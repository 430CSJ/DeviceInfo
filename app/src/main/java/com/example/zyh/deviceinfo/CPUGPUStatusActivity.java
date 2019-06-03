package com.example.zyh.deviceinfo;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.zyh.deviceinfo.CPUManager.getCpuName;
import static com.example.zyh.deviceinfo.CPUManager.getNumCores;

public class CPUGPUStatusActivity extends AppCompatActivity {

    public void exit() {
        Intent intent = new Intent();
        intent.setAction("com.example.zyh.deviceinfo.EXIT1");
        sendBroadcast(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AlertDialog.Builder aboutdialog = new AlertDialog.Builder(CPUGPUStatusActivity.this);
                aboutdialog.setTitle("About");
                aboutdialog.setMessage("DeviceInfo 1.0 by zyh.");
                aboutdialog.setCancelable(true);
                aboutdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface aboutdialog, int which) {

                    }
                });
                aboutdialog.show();
                break;
            case R.id.exit:
                exit();
                //android.os.Process.killProcess(android.os.Process.myPid());
        }
        return true;
    }

    CPUManager cpuManager0 = new CPUManager(-1);
    ArrayList<CPUManager> cpuManager = new ArrayList<CPUManager>();

    private LinearLayout[] llist = new LinearLayout[10];
    private TextView[] cminlist = new TextView[10];
    private TextView[] cmaxlist = new TextView[10];
    private TextView[] ccurlist = new TextView[10];
    private TextView[] cusalist = new TextView[10];
    private TextView[] collist = new TextView[10];

    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                for (int i = 0; i < getNumCores(); i++) {
                    if (cpuManager.get(i).isOnline())
                        collist[i].setText("Online");
                    else collist[i].setText("Offline");
                    cminlist[i].setText(cpuManager.get(i).getMinCpuFreq() + "MHz");
                    cmaxlist[i].setText(cpuManager.get(i).getMaxCpuFreq() + "MHz");
                    ccurlist[i].setText(cpuManager.get(i).getCurCpuFreq() + "MHz");
                    cusalist[i].setText(cpuManager.get(i).getUsage() + "%");
                }
                mHandler.postDelayed(this, 5000);
            } catch (Exception e) {

            }
        }
    };
/*
    private HorizontalScrollView cgsMain = (HorizontalScrollView) findViewById(R.id.cgsmain);
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void getFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            cgsMain.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpugpustatus_layout);
        //getFullScreen();

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        llist[0] = (LinearLayout) findViewById(R.id.ll_cpu0);
        llist[1] = (LinearLayout) findViewById(R.id.ll_cpu1);
        llist[2] = (LinearLayout) findViewById(R.id.ll_cpu2);
        llist[3] = (LinearLayout) findViewById(R.id.ll_cpu3);
        llist[4] = (LinearLayout) findViewById(R.id.ll_cpu4);
        llist[5] = (LinearLayout) findViewById(R.id.ll_cpu5);
        llist[6] = (LinearLayout) findViewById(R.id.ll_cpu6);
        llist[7] = (LinearLayout) findViewById(R.id.ll_cpu7);
        llist[8] = (LinearLayout) findViewById(R.id.ll_cpu8);
        llist[9] = (LinearLayout) findViewById(R.id.ll_cpu9);

        cminlist[0] = (TextView) findViewById(R.id.text_view_c0min);
        cminlist[1] = (TextView) findViewById(R.id.text_view_c1min);
        cminlist[2] = (TextView) findViewById(R.id.text_view_c2min);
        cminlist[3] = (TextView) findViewById(R.id.text_view_c3min);
        cminlist[4] = (TextView) findViewById(R.id.text_view_c4min);
        cminlist[5] = (TextView) findViewById(R.id.text_view_c5min);
        cminlist[6] = (TextView) findViewById(R.id.text_view_c6min);
        cminlist[7] = (TextView) findViewById(R.id.text_view_c7min);
        cminlist[8] = (TextView) findViewById(R.id.text_view_c8min);
        cminlist[9] = (TextView) findViewById(R.id.text_view_c9min);

        cmaxlist[0] = (TextView) findViewById(R.id.text_view_c0max);
        cmaxlist[1] = (TextView) findViewById(R.id.text_view_c1max);
        cmaxlist[2] = (TextView) findViewById(R.id.text_view_c2max);
        cmaxlist[3] = (TextView) findViewById(R.id.text_view_c3max);
        cmaxlist[4] = (TextView) findViewById(R.id.text_view_c4max);
        cmaxlist[5] = (TextView) findViewById(R.id.text_view_c5max);
        cmaxlist[6] = (TextView) findViewById(R.id.text_view_c6max);
        cmaxlist[7] = (TextView) findViewById(R.id.text_view_c7max);
        cmaxlist[8] = (TextView) findViewById(R.id.text_view_c8max);
        cmaxlist[9] = (TextView) findViewById(R.id.text_view_c9max);

        ccurlist[0] = (TextView) findViewById(R.id.text_view_c0cur);
        ccurlist[1] = (TextView) findViewById(R.id.text_view_c1cur);
        ccurlist[2] = (TextView) findViewById(R.id.text_view_c2cur);
        ccurlist[3] = (TextView) findViewById(R.id.text_view_c3cur);
        ccurlist[4] = (TextView) findViewById(R.id.text_view_c4cur);
        ccurlist[5] = (TextView) findViewById(R.id.text_view_c5cur);
        ccurlist[6] = (TextView) findViewById(R.id.text_view_c6cur);
        ccurlist[7] = (TextView) findViewById(R.id.text_view_c7cur);
        ccurlist[8] = (TextView) findViewById(R.id.text_view_c8cur);
        ccurlist[9] = (TextView) findViewById(R.id.text_view_c9cur);

        cusalist[0] = (TextView) findViewById(R.id.text_view_c0usa);
        cusalist[1] = (TextView) findViewById(R.id.text_view_c1usa);
        cusalist[2] = (TextView) findViewById(R.id.text_view_c2usa);
        cusalist[3] = (TextView) findViewById(R.id.text_view_c3usa);
        cusalist[4] = (TextView) findViewById(R.id.text_view_c4usa);
        cusalist[5] = (TextView) findViewById(R.id.text_view_c5usa);
        cusalist[6] = (TextView) findViewById(R.id.text_view_c6usa);
        cusalist[7] = (TextView) findViewById(R.id.text_view_c7usa);
        cusalist[8] = (TextView) findViewById(R.id.text_view_c8usa);
        cusalist[9] = (TextView) findViewById(R.id.text_view_c9usa);

        collist[0] = (TextView) findViewById(R.id.text_view_c0ol);
        collist[1] = (TextView) findViewById(R.id.text_view_c1ol);
        collist[2] = (TextView) findViewById(R.id.text_view_c2ol);
        collist[3] = (TextView) findViewById(R.id.text_view_c3ol);
        collist[4] = (TextView) findViewById(R.id.text_view_c4ol);
        collist[5] = (TextView) findViewById(R.id.text_view_c5ol);
        collist[6] = (TextView) findViewById(R.id.text_view_c6ol);
        collist[7] = (TextView) findViewById(R.id.text_view_c7ol);
        collist[8] = (TextView) findViewById(R.id.text_view_c8ol);
        collist[9] = (TextView) findViewById(R.id.text_view_c9ol);

        TextView textviewcn = (TextView) findViewById(R.id.text_view_cn);
        textviewcn.setText("Model Name: " + getCpuName());
        TextView textviewcminfreq = (TextView) findViewById(R.id.text_view_cminfreq);
        textviewcminfreq.setText("Min Frequency: " + Integer.parseInt(CPUGPUInfoActivity.getMinCpuFreqa()) /1000 + "MHz");
        TextView textviewcmaxfreq = (TextView) findViewById(R.id.text_view_cmaxfreq);
        textviewcmaxfreq.setText("Max Frequency: " + DeviceInfo.getCPUMaxFreqKHz() / 1000 + "MHz");
        TextView textviewccurfreq = (TextView) findViewById(R.id.text_view_ccurfreq);
        textviewccurfreq.setText(cpuManager0.getCurCpuFreq() + "MHz");
        TextView textviewcusage = (TextView) findViewById(R.id.text_view_cusage);
        textviewcusage.setText("Usage: " + cpuManager0.getCurCpuFreq() + "%");

        for (int i = 0; i < getNumCores(); i++) {
            cpuManager.add(new CPUManager(i));
            if (cpuManager.get(i).isOnline())
                collist[i].setText("Online");
            else collist[i].setText("Offline");
            cminlist[i].setText(cpuManager.get(i).getMinCpuFreq() + "MHz");
            cmaxlist[i].setText(cpuManager.get(i).getMaxCpuFreq() + "MHz");
            ccurlist[i].setText(cpuManager.get(i).getCurCpuFreq() + "MHz");
            cusalist[i].setText(cpuManager.get(i).getUsage() + "%");
        }
        for (int i = getNumCores(); i < llist.length; i++) {
            llist[i].setVisibility(View.GONE);
        }
        mHandler.postDelayed(runnable, 5000);
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}
