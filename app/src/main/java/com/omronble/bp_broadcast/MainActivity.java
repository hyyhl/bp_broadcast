package com.omronble.bp_broadcast;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.omron.lib.OMRONLib;
import com.omron.lib.common.OMRONBLEErrMsg;
import com.omron.lib.device.DeviceType;
import com.omron.lib.model.BPData;
import com.omronble.bp_broadcast.utils.CheckPermissionUtils;
import com.omronble.bp_broadcast.utils.UmsStringUtils;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static OMRONLib omronlib;
    private DeviceType deviceType = DeviceType.BLOOD_9200T;
    private CheckPermissionUtils mCheckPermissionUtils;
    private String bindDeviceId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        omronlib = OMRONLib.getInstance();
        boolean flag = omronlib.registerApp("123456", this);
        if(!flag){
            Toast.makeText(this,"初始化失败",Toast.LENGTH_SHORT).show();
            Log.v("OMRONLib", "初始化失败" );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initPermission();
    }

    @OnClick({R.id.wifi_fun, R.id.bt_fun, R.id.dev_fun,R.id.sys_info_fun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wifi_fun:
                Intent intent = new Intent();
                intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
                startActivity(intent);
                break;
            case R.id.bt_fun:
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                break;

            case R.id.dev_fun:  //暂放  绑定
                omronlib.bindDevice(this, deviceType, myOmronBleCallBack);
                break;

            case R.id.sys_info_fun: //暂放  获取数据
                omronlib.getDeviceData(this, deviceType,bindDeviceId,myOmronBleCallBack);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private OMRONLib.OmronBleCallBack myOmronBleCallBack = new OMRONLib.OmronBleCallBack() {
        @Override
        public void onFailure(OMRONBLEErrMsg errMsg) {}
        @Override
        public void onBindComplete(final String deviceName,final String deviceId){
            bindDeviceId = deviceId;
            //mTextMessage.setText("绑定成功-" + deviceName);
        }
        @Override
        public void onDataReadComplete(List<BPData> data) {}};

    /**
     * 初始化权限事件
     */
    private void initPermission() {
        //检查全部权限
        mCheckPermissionUtils = new CheckPermissionUtils(this);
        String[] permissions = mCheckPermissionUtils.checkMustPermissions(this.getApplicationContext());
        if (permissions.length == 0) {
            //权限都申请了
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    /**
     * EsayPermissions接管权限处理逻辑
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //检查必备权限
        String[] mustPermissions = mCheckPermissionUtils.checkMustPermissions(this.getApplicationContext());
        if (mustPermissions.length == 0) {

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, final List<String> perms) {
        String permissoin = "", content = "";
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) || perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            permissoin = "存储";
            content = getString(R.string.whyNeedPermissionStorage);
        }
        if (perms.contains(Manifest.permission.CALL_PHONE) || perms.contains(Manifest.permission.READ_PHONE_STATE)) {
            permissoin = UmsStringUtils.isNotEmpty(permissoin) ? "存储、电话" : "电话";
            content = content + getString(R.string.whyNeedPermissionPhone);
        }
        String openPermissionPath = getString(R.string.openPermissionPath, permissoin);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请")
                .setMessage(content + "\n" + openPermissionPath)
                .setPositiveButton("不允许", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("去设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                            mCheckPermissionUtils.getAppDetailSettingIntent(MainActivity.this, MainActivity.this, CheckPermissionUtils.REQUEST_MUST_PERM);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, mCheckPermissionUtils.checkMustPermissions(MainActivity.this), 100);
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
