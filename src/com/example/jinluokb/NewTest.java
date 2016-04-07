package com.example.jinluokb;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.pkusz.bluetooth.*;



//  final String[] categories = {"同事", "朋友", "家人", "客户"};对应的值分别为0、1、2、3

public class NewTest extends Activity {

//	int[] src={R.drawable.back,R.drawable.heartbeat,R.drawable.history,R.drawable.computer,R.drawable.history};

	 /** Called when the activity is first created. */
		public static final int REQUEST_ENABLE_BT = 8807;
		public BroadcastReceiver mBTReceiver;
		public static BluetoothSocket mBTSocket;
		public BluetoothAdapter mBTAdapter;
		private Button btnSearchDevices;
		private ToggleButton tBtnBTSwitch;
		private BluetoothDevice mBTDevice;
		private BluetoothDevice  remoteDevice;
		private ArrayAdapter<String> adtDvcs;
		private List<String> lstDvcsStr = new ArrayList<String>();	
		private ListView lvDevicesList;
		Bundle bundle;
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newtest);

// 接收到数据是  类别，和  时间，以此来判断用户在表中的位置,直接传递到下一步，RTMonitorActivity
	    bundle = new Bundle();
        bundle = this.getIntent().getExtras();    
      
//蓝牙部分
    // Init Bluetooth Adapter
    mBTAdapter = BluetoothAdapter.getDefaultAdapter();
    
    if (mBTAdapter == null){
    	Toast.makeText(NewTest.this, " 设备不支持蓝牙 ", Toast.LENGTH_SHORT).show();
    	this.finish();
    }
    
    // Set up BroadCast Receiver
    mBTReceiver = new BroadcastReceiver(){
    	public void onReceive(Context context,Intent intent){
    		String act = intent.getAction();
    		// To see whether the action is that already found devices
    		if(act.equals(BluetoothDevice.ACTION_FOUND)){
    			// If found one device, get the device object
    			BluetoothDevice tmpDvc = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    			// Put the name & address into a string
    			String tmpDvcStr = tmpDvc.getName()+"|"+tmpDvc.getAddress();
    			if (lstDvcsStr.indexOf(tmpDvcStr)==-1){
    				// Avoid duplicate add devices
    				lstDvcsStr.add(tmpDvcStr);
    				adtDvcs.notifyDataSetChanged();
    				Toast.makeText(NewTest.this, "发现新设备", Toast.LENGTH_SHORT).show();
    			}
    		}
    		if(act.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
    			Toast.makeText(NewTest.this, " 搜索完成", Toast.LENGTH_SHORT).show();
    		}
   		
    		if (act.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
				Toast.makeText(NewTest.this, "开始搜索设备", Toast.LENGTH_SHORT).show();
    		}
     	}
    };
    
    //Register the broadcastReceiver
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(mBTReceiver,filter);
    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    registerReceiver(mBTReceiver,filter);
    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    registerReceiver(mBTReceiver,filter); 
    
    // Init Buttons
    btnSearchDevices = (Button)findViewById(R.id.btnSearchDevice);
    tBtnBTSwitch = (ToggleButton)findViewById(R.id.tBtnBTSwitch);
    lvDevicesList = (ListView)findViewById(R.id.lvDevicesList);
    
    if(mBTAdapter.getState()==BluetoothAdapter.STATE_OFF)
    	tBtnBTSwitch.setChecked(false);
    if(mBTAdapter.getState()==BluetoothAdapter.STATE_ON)
    	tBtnBTSwitch.setChecked(true);

    // ListView And Data Adapter
    adtDvcs = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lstDvcsStr);
    lvDevicesList.setAdapter(adtDvcs);
    
    // Add Click Listeners to Buttons
    tBtnBTSwitch.setOnClickListener(new OnClickListener(){
    	@Override
    	public void onClick(View view){
            // Open BT
    		if(tBtnBTSwitch.isChecked()){
    			if(!mBTAdapter.isEnabled()){
        			//Open a new dialog to ask user whether wanna open BT
                	Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                	startActivityForResult(enabler,REQUEST_ENABLE_BT);       				
    			}
    			tBtnBTSwitch.setChecked(true);
    		}
    		
    		// Close BT
    		if(!tBtnBTSwitch.isChecked()){
    			if(mBTAdapter.isEnabled()){
        			mBTAdapter.disable();
        			Toast.makeText(NewTest.this, "已关闭", Toast.LENGTH_SHORT).show();      				
    			}
    			tBtnBTSwitch.setChecked(false);
    		}
    	}
    });
    
    // Search Devices
    btnSearchDevices.setOnClickListener(new OnClickListener(){
    	@Override
    	public void onClick(View view){
    		if (mBTAdapter.isDiscovering()){
    			Toast.makeText(NewTest.this, "搜寻中", Toast.LENGTH_SHORT).show();
    		}
			else{
				lstDvcsStr.clear();
				adtDvcs.notifyDataSetChanged();
				mBTDevice = null;
				mBTAdapter.startDiscovery();
			}
    	}
    });
    
    // Create Click listener for the items on devices list
    lvDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
    	@Override
    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3){
    		if (mBTAdapter == null)
    			Toast.makeText(NewTest.this, "没有设备支持蓝牙h", Toast.LENGTH_SHORT).show();
    		else{
    			// stop searching
    			mBTAdapter.cancelDiscovery();
    			// Get address of remote device
    			String str = lstDvcsStr.get(arg2);
    			String[] dvcValues = str.split("\\|");
    			String dvcAddr = dvcValues[1];
    			//UUID dvcUUID = UUID.randomUUID();
    			//00001101-0000-1000-8000-00805F9B34FB SPP protocal
    			UUID dvcUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    			// Set BT device
    			mBTDevice = mBTAdapter.getRemoteDevice(dvcAddr);
    			
//    			if(mBTDevice.getBondState() != BluetoothDevice.BOND_BONDED){//判断给定地址下的device是否已经配对  
//    		         try{  
//    		              autoBond(mBTDevice.getClass(), mBTDevice, "8888");//设置pin值  
//    		              createBond(mBTDevice.getClass(), mBTDevice);  
//    		              remoteDevice = mBTDevice;  
//    		          }  
//    		          catch (Exception e) {  
//    		           // TODO: handle exception  
//    		          System.out.println("配对不成功");  
//    		          }  
//    		 }  
//    		else {  
//    		          remoteDevice = mBTDevice;  
//    		}  
    		  
    			// Connect Device
    			try{
    				mBTSocket = mBTDevice.createRfcommSocketToServiceRecord(dvcUUID);
    				mBTSocket.connect();
    				Intent mInt = new Intent(NewTest.this,RTMonitorActivity.class);
    				mInt.putExtras(bundle);
    				startActivity(mInt);
    			}
    			catch(IOException e){
    				e.printStackTrace();
    			}
    		}        		
    	}
    });   
	}
	
	
	
    public void onActivityResult(int RequestCode, int ResultCode,Intent data){
    	switch(RequestCode){
    	case REQUEST_ENABLE_BT:
    		if(ResultCode == RESULT_OK){
//    			Toast.makeText(this.getApplicationContext(), "BT Launched!", Toast.LENGTH_SHORT).show();
    		}
    		else
    			if(ResultCode == RESULT_CANCELED){
        			Toast.makeText(this.getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();   				
    			}
    		break;
    	}
    }    
    @Override
	protected void onDestroy() {
	    this.unregisterReceiver(mBTReceiver);
		super.onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
//	//自动配对设置Pin值  
//    static public boolean autoBond(Class btClass,BluetoothDevice device,String strPin) throws Exception {   
//        Method autoBondMethod = btClass.getMethod("setPin",new Class[]{byte[].class});  
//        Boolean result = (Boolean)autoBondMethod.invoke(device,new Object[]{strPin.getBytes()});  
//        return result;  
//    }  
//  
////开始配对  
//    static public boolean createBond(Class btClass,BluetoothDevice device) throws Exception {   
//        Method createBondMethod = btClass.getMethod("createBond");   
//        Boolean returnValue = (Boolean) createBondMethod.invoke(device);   
//        return returnValue.booleanValue();   
//    } 
	
	
	
}

