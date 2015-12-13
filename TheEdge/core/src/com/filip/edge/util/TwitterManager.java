package com.filip.edge.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import de.tomgrill.gdxtwitter.core.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fkrstevski on 2015-12-08.
 */
public class TwitterManager {
    public static final String TAG = TwitterManager.class.getName();
    public static final TwitterManager instance = new TwitterManager();

    public TwitterAPI twitterAPI;

    private TwitterManager() {

    }

    public void load() {
        if (twitterAPI == null) {
            TwitterSystem twitterSystem = new TwitterSystem(new MyTwitterConfig());

            twitterAPI = twitterSystem.getTwitterAPI();
            twitterAPI.setTokenAndSecret("1213562396-xqtnrInv9YW1d0jU9nqe3UIIUDPJmaUgMfgVW5A", "7d2BSsX1bS7Rzg7epZk9J6YxyP9oQS2WBriWRiJ5ANXkZ");

            autoSignIn();
        }
    }

    public boolean isUserSignedIn() {
        return (twitterAPI != null && twitterAPI.isLoaded() && twitterAPI.isSignedin());
    }

    public void autoSignIn() {
        if (twitterAPI.isLoaded() && !twitterAPI.isSignedin()) {
            twitterAPI.signin(false, new TwitterResponseListener() {
                @Override
                public void success(String data) {
                    System.out.println("Twitter Signin successfull" + data);

                }

                @Override
                public void apiError(HttpStatus response, String data) {
                    System.out.println("Twitter Signin with API error: " + data);

                }

                @Override
                public void httpError(Throwable t) {
                    System.out.println("Twitter Signin with http error: " + t.getMessage());

                }

                @Override
                public void cancelled() {
                    System.out.println("Twitter Signin canceled");

                }
            });
        }
    }

    public void signinUserWithGUI() {
        twitterAPI.signin(true, new TwitterResponseListener() {

            @Override
            public void success(String data) {
                Gdx.app.debug(TAG, "GUI Signin successfull" + data);

            }

            @Override
            public void apiError(HttpStatus response, String data) {
                Gdx.app.debug(TAG, "GUI Signin with error: " + data);

            }

            @Override
            public void httpError(Throwable t) {
                Gdx.app.debug(TAG, "GUI Signin httpError" + t.getMessage());

            }

            @Override
            public void cancelled() {
                Gdx.app.debug(TAG, "GUI Signin canceled");

            }
        });
    }

    private void uploadTwitterPhoto(String status, String media_id) {

        String mediaIdString = media_id;
        TwitterRequest tweetTextRequest = new TwitterRequest(TwitterRequestType.POST, "https://api.twitter.com/1.1/statuses/update.json");
        tweetTextRequest.put("status", status);
        tweetTextRequest.put("media_ids", mediaIdString);

        twitterAPI.newAPIRequest(tweetTextRequest, new TwitterResponseListener() {
            @Override
            public void success(String data) {
                System.out.println("success2: " + data);

            }

            @Override
            public void apiError(HttpStatus response, String data) {
                System.out.println("apiError " + data);
            }

            @Override
            public void httpError(Throwable t) {
                System.out.println("httpError");
            }

            @Override
            public void cancelled() {
                System.out.println("cancelled");
            }
        });
    }

    public void uploadStatus(String message) {
        TwitterRequest tweetTextRequest = new TwitterRequest(TwitterRequestType.POST, "https://api.twitter.com/1.1/statuses/update.json");
        tweetTextRequest.put("status", message);

        twitterAPI.newAPIRequest(tweetTextRequest, new TwitterResponseListener() {

            @Override
            public void cancelled() {
                System.out.println("cancelled");

            }

            @Override
            public void success(String data) {
                System.out.println("success: " + data);

            }

            @Override
            public void apiError(HttpStatus response, String data) {
                System.out.println("apiError " + data);

            }

            @Override
            public void httpError(Throwable t) {
                System.out.println("httpError" + t.getMessage());

            }
        });
    }

    private String generateImageData(FileHandle handle) {
        File file = handle.file();

        byte[] bytes = new byte[0];
        try {
            bytes = loadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        char[] encoded = Base64Coder.encode(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public void uploadPhoto(final String message) {
        FileHandle file = new FileHandle(Gdx.files.getLocalStoragePath() + "shot.png");
        Gdx.app.log(TAG, "file exist " + file.exists());

        TwitterRequest tr = new TwitterRequest(TwitterRequestType.POST, "https://upload.twitter.com/1.1/media/upload.json");
        tr.put("media_data", generateImageData(file));
        twitterAPI.newAPIRequest(tr, new TwitterResponseListener() {

            @Override
            public void cancelled() {
                System.out.println("cancelled");

            }

            @Override
            public void success(String data) {
                System.out.println("success: " + data);
                JsonValue root = new JsonReader().parse(data);
                String media_id = root.get(1).asString();
                Gdx.app.log(TAG, "parsed media id " + media_id);
                uploadTwitterPhoto(message, media_id);

            }

            @Override
            public void apiError(HttpStatus response, String data) {
                System.out.println("apiError " + data + ", response " + response.getStatusCode());

            }

            @Override
            public void httpError(Throwable t) {
                System.out.println("httpError " + t.getMessage());

            }
        });
    }
}
