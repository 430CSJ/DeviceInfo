package com.example.zyh.deviceinfo;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.zyh.deviceinfo.DeviceInfo.getTotalMemory;

public class MainActivity extends AppCompatActivity {

    public int blevel;
    public int bvoltage;
    public double btemperature;
    public String btechnology;
    public String bstatus;
    public String bplugged;
    public String bhealth;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
            unregisterReceiver(this);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.zyh.deviceinfo.EXIT");
        registerReceiver(this.broadcastReceiver, filter);
    }

    //private BatteryReceiver batteryReceiver;

    public String[] getVersion() {
        String[] version = {"null", "null", "null", "null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0] = arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
            version[0] = "Fail to get kernel version.";
        }
        version[1] = Build.VERSION.RELEASE;//FirmwareVersion
        version[2] = Build.MODEL;//Model
        version[3] = Build.DISPLAY;//SystemVersion
        return version;
    }

    public String getChargeFullDesign() {
        String str1 = "/sys/class/power_supply/battery/charge_full_design";
        String str2;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = String.valueOf(Integer.parseInt(localBufferedReader.readLine()) / 1024) + "mAh";
            localBufferedReader.close();
        } catch (IOException e) {
            str2 = "Fail to get capacity of battery.";
        }
        return str2;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getDisplayResolution(Point point) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealSize(point);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            getWindowManager().getDefaultDisplay().getSize(point);
        } else {
            point.x = getWindowManager().getDefaultDisplay().getWidth();
            point.y = getWindowManager().getDefaultDisplay().getHeight();
        }
    }

    public String getAvailMemory() {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
    }

    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    public long[] getRootMemory() {
        long[] rootInfo = new long[2];
        File path = Environment.getRootDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        rootInfo[0] = blockSize * totalBlocks;
        rootInfo[1] = blockSize * availableBlocks;
        return rootInfo;
    }

    public long[] getSDCardMemory() {
        long[] sdCardInfo=new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
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
                AlertDialog.Builder aboutdialog = new AlertDialog.Builder(MainActivity.this);
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
                finish();
                //android.os.Process.killProcess(android.os.Process.myPid());
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        TextView textviewkvd = (TextView) findViewById(R.id.text_view_mkvd);
        textviewkvd.setText(getVersion()[0]);
        TextView textviewfvd = (TextView) findViewById(R.id.text_view_mfvd);
        textviewfvd.setText(getVersion()[1]);
        TextView textviewapil = (TextView) findViewById(R.id.text_view_mapil);
        textviewapil.setText(Build.VERSION.SDK);
        TextView textviewmd = (TextView) findViewById(R.id.text_view_mmd);
        textviewmd.setText(getVersion()[2]);
        TextView textviewsvd = (TextView) findViewById(R.id.text_view_msvd);
        textviewsvd.setText(getVersion()[3]);

        Button buttoncg = (Button) findViewById(R.id.button_mcg);
        buttoncg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CPUGPUInfoActivity.class);
                startActivity(intent);
            }
        });

        Point point = new Point();
        getDisplayResolution(point);
        TextView textviewdr = (TextView) findViewById(R.id.text_view_mdr);
        textviewdr.setText(point.x + " × " + point.y);
        TextView textviewdid = (TextView) findViewById(R.id.text_view_mdid);
        textviewdid.setText(String.valueOf(getResources().getDisplayMetrics().density));
        TextView textviewdpi = (TextView) findViewById(R.id.text_view_mdpi);
        textviewdpi.setText(String.valueOf(getResources().getDisplayMetrics().densityDpi));
        TextView textviewxydpi = (TextView) findViewById(R.id.text_view_mxydpi);
        textviewxydpi.setText(getResources().getDisplayMetrics().xdpi + " / " + getResources().getDisplayMetrics().ydpi);
        TextView textviewsz = (TextView) findViewById(R.id.text_view_msz);
        textviewsz.setText(point.x / getResources().getDisplayMetrics().xdpi * 25.4 + " mm × " + point.y / getResources().getDisplayMetrics().ydpi * 25.4 + " mm");
        TextView textviewsi = (TextView) findViewById(R.id.text_view_msi);
        textviewsi.setText(String.valueOf(Math.sqrt(Math.pow(point.x / getResources().getDisplayMetrics().xdpi, 2) + Math.pow(point.y / getResources().getDisplayMetrics().ydpi, 2))));

        Button buttonrefreshstorageusage = (Button) findViewById(R.id.button_mrefreshstorageusage);
        buttonrefreshstorageusage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textviewram = (TextView) findViewById(R.id.text_view_mram);
                textviewram.setText(getAvailMemory() + "/" + getTotalMemory(getApplicationContext()) / 1024 / 1024 + " MB");
                TextView textviewroot = (TextView) findViewById(R.id.text_view_mroot);
                textviewroot.setText(getRootMemory()[1] / 1024 / 1024 + " MB/" + getRootMemory()[0] / 1024 / 1024 + " MB");
                TextView textviewdd = (TextView) findViewById(R.id.text_view_mdd);
                textviewdd.setText(Environment.getDataDirectory().toString());
                TextView textviewrom = (TextView) findViewById(R.id.text_view_mrom);
                textviewrom.setText(getAvailableInternalMemorySize() / 1024 / 1024 + " MB/" + getTotalInternalMemorySize() / 1024 / 1024 + " MB");
                TextView textviewsdd = (TextView) findViewById(R.id.text_view_msdd);
                textviewsdd.setText(Environment.getExternalStorageDirectory().toString());
                TextView textviewsd = (TextView) findViewById(R.id.text_view_msd);
                textviewsd.setText(getSDCardMemory()[1] / 1024 / 1024 + " MB/" + getSDCardMemory()[0] / 1024 / 1024 + " MB");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String tstr = sdf.format(date);
                TextView textviewsudt = (TextView) findViewById(R.id.text_view_msudt);
                textviewsudt.setText("Storage usage data at " + tstr + ".");
                Toast.makeText(MainActivity.this, "Refresh storage usage data successfully.", Toast.LENGTH_SHORT).show();
            }
        });
        TextView textviewram = (TextView) findViewById(R.id.text_view_mram);
        textviewram.setText(getAvailMemory() + "/" + getTotalMemory(getApplicationContext()) / 1024 / 1024 + " MB");
        TextView textviewrootd = (TextView) findViewById(R.id.text_view_mrootd);
        textviewrootd.setText(Environment.getRootDirectory().toString());
        TextView textviewroot = (TextView) findViewById(R.id.text_view_mroot);
        textviewroot.setText(getRootMemory()[1] / 1024 / 1024 + " MB/" + getRootMemory()[0] / 1024 / 1024 + " MB");
        TextView textviewdd = (TextView) findViewById(R.id.text_view_mdd);
        textviewdd.setText(Environment.getDataDirectory().toString());
        TextView textviewrom = (TextView) findViewById(R.id.text_view_mrom);
        textviewrom.setText(getAvailableInternalMemorySize() / 1024 / 1024 + " MB/" + getTotalInternalMemorySize() / 1024 / 1024 + " MB");
        TextView textviewsdd = (TextView) findViewById(R.id.text_view_msdd);
        textviewsdd.setText(Environment.getExternalStorageDirectory().toString());
        TextView textviewsd = (TextView) findViewById(R.id.text_view_msd);
        textviewsd.setText(getSDCardMemory()[1] / 1024 / 1024 + " MB/" + getSDCardMemory()[0] / 1024 / 1024 + " MB");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        String tstr = sdf.format(date);
        TextView textviewsudt = (TextView) findViewById(R.id.text_view_msudt);
        textviewsudt.setText("Storage usage data at " + tstr + ".");

    }
    public BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (intent.ACTION_BATTERY_CHANGED.equals(action)) {
                blevel = intent.getIntExtra("level", 0);
                bvoltage = intent.getIntExtra("voltage", 0);
                btemperature = intent.getIntExtra("temperature", 0);
                btemperature /= 10;
                btechnology = intent.getStringExtra("technology");
                switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        bstatus = "Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        bstatus = "Discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        bstatus = "Not Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        bstatus = "Full";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        bstatus = "Unknown";
                        break;
                    default:
                        bstatus = "Fail to get battery status.";
                }
                switch (intent.getIntExtra("plugged", 0)) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        bplugged = "AC";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        bplugged = "USB";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        bplugged = "Wireless";
                        break;
                }
                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        bhealth = "Unknown";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        bhealth = "Good";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        bhealth = "Dead";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        bhealth = "Over Voltage";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        bhealth = "Overheat";
                        break;
                    case BatteryManager.BATTERY_HEALTH_COLD:
                        bhealth = "Cold";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        bhealth = "Unspecified Failure";
                        break;
                }
            }
            TextView textviewbl = (TextView) findViewById(R.id.text_view_mbl);
            textviewbl.setText(blevel + "%");
            TextView textviewbv = (TextView) findViewById(R.id.text_view_mbv);
            textviewbv.setText(bvoltage + "mV");
            TextView textviewbtemp = (TextView) findViewById(R.id.text_view_mbtemp);
            textviewbtemp.setText(btemperature + "℃");
            TextView textviewbtech = (TextView) findViewById(R.id.text_view_mbtech);
            textviewbtech.setText(btechnology);
            TextView textviewbs = (TextView) findViewById(R.id.text_view_mbs);
            textviewbs.setText(bstatus);
            TextView textviewbp = (TextView) findViewById(R.id.text_view_mbp);
            textviewbp.setText(bplugged);
            TextView textviewbh = (TextView) findViewById(R.id.text_view_mbh);
            textviewbh.setText(bhealth);
            TextView textviewbc = (TextView) findViewById(R.id.text_view_mbc);
            textviewbc.setText(getChargeFullDesign());
        }
    };
}
