package zhaohad.glpath.c1_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    boolean mIsSupportES2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(getBaseContext());

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo conf = am.getDeviceConfigurationInfo();

        /**
         * reqGlEsVersion
         * The GLES version used by an application. The upper order 16 bits represent the
         * major version and the lower order 16 bits the minor version.
         */
        mIsSupportES2 = conf.reqGlEsVersion >= 0x20000;
        Log.e("hanwei", "conf.reqGlEsVersion = " + conf.reqGlEsVersion);
        mGLSurfaceView.setRenderer(new GLRender());

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsSupportES2) {
            mGLSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsSupportES2) {
            mGLSurfaceView.onPause();
        }
    }
}