package com.example.zyh.deviceinfo;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TestGPUActivity extends AppCompatActivity {

    String[] GPUDetail = new String[4];

    class DemoRenderer implements GLSurfaceView.Renderer {


        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            Log.d("SystemInfo", "GL_RENDERER = " +gl.glGetString(GL10.GL_RENDERER));
            Log.d("SystemInfo", "GL_VENDOR = " + gl.glGetString(GL10.GL_VENDOR));
            Log.d("SystemInfo", "GL_VERSION = " + gl.glGetString(GL10.GL_VERSION));
            Log.i("SystemInfo", "GL_EXTENSIONS = " + gl.glGetString(GL10.GL_EXTENSIONS));
            GPUDetail[0] = gl.glGetString(GL10.GL_RENDERER);
            GPUDetail[1] = gl.glGetString(GL10.GL_VENDOR);
            GPUDetail[2] = gl.glGetString(GL10.GL_VERSION);
            GPUDetail[3] = gl.glGetString(GL10.GL_EXTENSIONS);
            Intent intent = new Intent();
            intent.putExtra("data_return", GPUDetail);
            setResult(RESULT_OK, intent);
            finish();
        }


        @Override
        public void onDrawFrame(GL10 arg0) {
            // TODO Auto-generated method stub

        }


        @Override
        public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub

        }

    }

    class DemoGLSurfaceView extends GLSurfaceView {

        DemoRenderer mRenderer;
        public DemoGLSurfaceView(Context context) {
            super(context);
            setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            mRenderer = new DemoRenderer();
            setRenderer(mRenderer);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glView = new DemoGLSurfaceView(this);
        this.setContentView(glView);
    }
}
