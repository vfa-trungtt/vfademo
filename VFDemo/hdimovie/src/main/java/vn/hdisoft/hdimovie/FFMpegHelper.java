package vn.hdisoft.hdimovie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class FFMpegHelper {
    public interface OnProccessVideoListener{
        public void onProcessDone(int errorCode,String errorMessage);
    }
    private FFmpeg ffmpeg;


    private int  errorCode = 0;

    private OnProccessVideoListener _listener;

    public void setOnProcessVideo(OnProccessVideoListener listener){
        _listener = listener;
    }

    public  FFMpegHelper(Context context){
        try {
            if (ffmpeg == null) {
                ffmpeg = FFmpeg.getInstance(context);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                }

                @Override
                public void onSuccess() {
                }
            });
        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
        }
    }

    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
//                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
//                    Log.d(TAG, "SUCCESS with output : " + s);
//                    if (choice == 1 || choice == 2 || choice == 5 || choice == 6 || choice == 7) {
//                        Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
//                        intent.putExtra(FILEPATH, filePath);
//                        startActivity(intent);
//                    } else if (choice == 3) {
//                        Intent intent = new Intent(MainActivity.this, PreviewImageActivity.class);
//                        intent.putExtra(FILEPATH, filePath);
//                        startActivity(intent);
//                    } else if (choice == 4) {
//                        Intent intent = new Intent(MainActivity.this, AudioPreviewActivity.class);
//                        intent.putExtra(FILEPATH, filePath);
//                        startActivity(intent);
//                    } else if (choice == 8) {
//                        choice = 9;
//                        reverseVideoCommand();
//                    } else if (Arrays.equals(command, lastReverseCommand)) {
//                        choice = 10;
//                        concatVideoCommand();
//                    } else if (choice == 10) {
//                        File moviesDir = Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_MOVIES
//                        );
//                        File destDir = new File(moviesDir, ".VideoPartsReverse");
//                        File dir = new File(moviesDir, ".VideoSplit");
//                        if (dir.exists())
//                            deleteDir(dir);
//                        if (destDir.exists())
//                            deleteDir(destDir);
//                        choice = 11;
//                        Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
//                        intent.putExtra(FILEPATH, filePath);
//                        startActivity(intent);
//                    }
                }

                @Override
                public void onProgress(String s) {
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//                    if (choice == 8)
//                        progressDialog.setMessage("progress : splitting video " + s);
//                    else if (choice == 9)
//                        progressDialog.setMessage("progress : reversing splitted videos " + s);
//                    else if (choice == 10)
//                        progressDialog.setMessage("progress : concatenating reversed videos " + s);
//                    else
//                        progressDialog.setMessage("progress : " + s);
//                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//                    progressDialog.setMessage("Processing...");
//                    progressDialog.show();
                }

                @Override
                public void onFinish() {
//                    Log.d(TAG, "Finished command : ffmpeg " + command);
//                    if (choice != 8 && choice != 9 && choice != 10) {
//                        progressDialog.dismiss();
//                    }
                    if(_listener != null) _listener.onProcessDone(0,"");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    public void cropVideo(String srcPath, String cropInfo,String destPath){
        String[] complexCommand = { "-i", srcPath,"-filter:v", "crop=80:60:200:100", "-c:a","copy", destPath};
        execFFmpegBinary(complexCommand);
    }

    public void addWatermark(String filePath){

    }
}
