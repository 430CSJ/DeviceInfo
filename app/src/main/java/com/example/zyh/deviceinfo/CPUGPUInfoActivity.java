package com.example.zyh.deviceinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.zyh.deviceinfo.DeviceInfo.DEVICEINFO_UNKNOWN;
import static com.example.zyh.deviceinfo.DeviceInfo.getCPUMaxFreqKHz;
import static com.example.zyh.deviceinfo.DeviceInfo.getNumberOfCPUCores;

public class CPUGPUInfoActivity extends AppCompatActivity {

    public void exit() {
        Intent intent = new Intent();
        intent.setAction("com.example.zyh.deviceinfo.EXIT");
        sendBroadcast(intent);
        finish();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this);
            exit();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.zyh.deviceinfo.EXIT1");
        registerReceiver(this.broadcastReceiver, filter);
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
                AlertDialog.Builder aboutdialog = new AlertDialog.Builder(CPUGPUInfoActivity.this);
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
/*
    public static String getCPUName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
    public static String getMinCpuFreqa() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    public static int[] getGPUMinMaxFreq() {
        int maxFreq = DEVICEINFO_UNKNOWN;
        int[] freq = new int[2];
        String filepath = "/sys/class/devfreq/";
        File file = new File(filepath);
        File[] fileList = file.listFiles();
        List<File> wjjList = new ArrayList<File>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory())
                wjjList.add(fileList[i]);
        }
        String filename;
        for (int i = 0; i < wjjList.size(); i++) {
            if (wjjList.get(i).getName().contains("kgsl")) {
                filename = filepath + wjjList.get(i).getName() + "/" + "min_freq";
                File gpuInfoMinFreqFile = new File(filename);
                if (gpuInfoMinFreqFile.exists()) {
                    try {
                        FileReader localFileReader = new FileReader(filename);
                        BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                        freq[0] = Integer.parseInt(localBufferedReader.readLine()) / 1000000;
                        localBufferedReader.close();
                    } catch (IOException e) {

                    }
                }
                filename = filepath + wjjList.get(i).getName() + "/" + "max_freq";
                File gpuInfoMaxFreqFile = new File(filename);
                if (gpuInfoMaxFreqFile.exists()) {
                    try {
                        FileReader localFileReader = new FileReader(filename);
                        BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                        freq[1] = Integer.parseInt(localBufferedReader.readLine()) / 1000000;
                        localBufferedReader.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
        return freq;
    }

    String[] GPUInfo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    GPUInfo = data.getStringArrayExtra("data_return");
                }
                TextView textviewgven = (TextView) findViewById(R.id.text_view_gven);
                textviewgven.setText("Vender: " + GPUInfo[1]);
                TextView textviewgn = (TextView) findViewById(R.id.text_view_gn);
                textviewgn.setText(GPUInfo[0]);
                TextView textviewglver = (TextView) findViewById(R.id.text_view_glver);
                textviewglver.setText("GL Version: " + GPUInfo[2]);
                TextView textviewgext = (TextView) findViewById(R.id.text_view_gext);
                textviewgext.setText(GPUInfo[3]);
                break;
            default:
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpugpuinfo_layout);
        Intent intent = new Intent(CPUGPUInfoActivity.this, TestGPUActivity.class);
        startActivityForResult(intent, 1);

        Button buttoncgs = (Button) findViewById(R.id.button_cgs);
        buttoncgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CPUGPUInfoActivity.this, CPUGPUStatusActivity.class);
                startActivity(intent);
            }
        });

        TextView textviewcnc = (TextView) findViewById(R.id.text_view_cnc);
        textviewcnc.setText(cpuManager0.getCpuName() + ",\n" + cpuManager0.getNumCores() + " core(s) processor.");
        TextView textviewcabi = (TextView) findViewById(R.id.text_view_cabi);
        textviewcabi.setText("ABI: "  + Build.CPU_ABI);
        TextView textviewcabi2 = (TextView) findViewById(R.id.text_view_cabi2);
        textviewcabi2.setText("ABI2: " + Build.CPU_ABI2);
        TextView textviewcminf = (TextView) findViewById(R.id.text_view_cminf);
        textviewcminf.setText("Min Frequency: " + Integer.parseInt(getMinCpuFreqa()) / 1000 + "MHz");
        TextView textviewcmaxf = (TextView) findViewById(R.id.text_view_cmaxf);
        textviewcmaxf.setText("Max Frequency: " + getCPUMaxFreqKHz() / 1000 + "MHz");

        TextView textviewgminf = (TextView) findViewById(R.id.text_view_gminf);
        textviewgminf.setText("Min Frequency: " + getGPUMinMaxFreq()[0] + "MHz");
        TextView textviewgmaxf = (TextView) findViewById(R.id.text_view_gmaxf);
        textviewgmaxf.setText("Max Frequency: " + getGPUMinMaxFreq()[1] + "MHz");
    }
}
