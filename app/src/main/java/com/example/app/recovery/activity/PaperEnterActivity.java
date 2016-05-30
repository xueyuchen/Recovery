package com.example.app.recovery.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app.recovery.R;
import com.example.app.recovery.enums.CarType;
import com.example.app.recovery.model.Paper;
import com.example.app.recovery.service.PaperService;
import com.example.app.recovery.tools.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by app on 16/4/10.
 */
public class PaperEnterActivity extends Activity {

    private Handler company_handler;
    private Handler garage_handler;
    private String company_list_str;
    private String garage_list_str;
    private JSONArray company_list;
    private JSONArray garage_list;
    private Camera camera;
    private Boolean isPreview = false;
    private Paper paper = new Paper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paper_enter);
        Button btn_back = (Button) findViewById(R.id.btn_paperEnter_back);
        Button btn_entry = (Button) findViewById(R.id.btn_paperEnter_entry);
        Spinner slt_garage = (Spinner) findViewById(R.id.select_garage_id);
        Spinner slt_company = (Spinner) findViewById(R.id.select_company_id);
        RadioGroup radio_type = (RadioGroup) this.findViewById(R.id.radio_type);
        Button btn_camera_open = (Button) findViewById(R.id.btn_camera_open);
        /**
         * 返回按钮事件
         */
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 拍照按钮事件
         */
        btn_camera_open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO 调用拍照 拍照后在"残值详细"下增加一个新的form表单,form表单中需要显示照片及一个input框
            }
        });
        /**
         * 录入按钮事件
         */
        btn_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reportCode = (EditText) findViewById(R.id.txt_report_code);
                EditText carLisense = (EditText) findViewById(R.id.txt_car_license);
                paper.setCar_license(carLisense.getText().toString());
                paper.setPape_code(PaperService.getNewPaperCode());
                paper.setReport_code(reportCode.getText().toString());
                System.out.println("*************************");
                System.out.println(paper.toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("send paper begin");
                        post_paper();
                    }
                }).start();
            }
        });
        /**
         * radio选择事件
         */
        radio_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) PaperEnterActivity.this.findViewById(radioButtonId);
                Log.i("radio", rb.getText().toString());
                paper.setType(CarType.findType(rb.getText().toString()));
            }
        });
        /**
         * 修理厂select选择事件
         */
        slt_garage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if (garage_list.length() != 0) {
                    Integer garage_id = null;
                    JSONObject jsonObject2 = (JSONObject) garage_list.opt(pos);
                    try {
                        garage_id = (Integer) jsonObject2.get("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paper.setGarage_id(garage_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paper.setGarage_id(null);
            }
        });
        /**
         * 残保险公司select选择事件
         */
        slt_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if (company_list.length() != 0) {
                    Integer company_id = null;
                    JSONObject jsonObject2 = (JSONObject) company_list.opt(pos);
                    try {
                        company_id = (Integer) jsonObject2.get("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paper.setCompany_id(company_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paper.setCompany_id(null);
            }
        });
        /**
         * fetch修理厂列表
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("send garage begin");
                fetch_garage();
                Message companyListMesg = garage_handler.obtainMessage();
                garage_handler.sendMessage(companyListMesg);
            }
        }).start();
        /**
         * fetch保险公司列表
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("send company begin");
                fetch_company();
                Message companyListMesg = company_handler.obtainMessage();
                company_handler.sendMessage(companyListMesg);
            }
        }).start();
        garage_handler = new Handler() {
            public void handleMessage(Message msg) {
                Spinner slt_garage = (Spinner) findViewById(R.id.select_garage_id);
                if (garage_list_str != null) {
                    System.out.println(garage_list_str);
                    try {
//                        System.out.println(msg.obj);
                        JSONArray jsonArray = new JSONArray(String.valueOf(garage_list_str));
                        garage_list = jsonArray;
                        System.out.println(garage_list.toString());
                        String[] names = new String[garage_list.length()];
                        for (int i = 0; i < garage_list.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) garage_list.opt(i);
                            System.out.println(jsonObject2.get("name"));
                            names[i] = (String) jsonObject2.get("name");
                            Integer garage_id = (Integer) jsonObject2.get("id");
                            if (i == garage_list.length() - 1) {
                                paper.setGarage_id(garage_id);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PaperEnterActivity.this, android.R.layout.simple_spinner_item, names);
                        //绑定 Adapter到控件
                        slt_garage.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };
        company_handler = new Handler() {
            public void handleMessage(Message msg) {
                Spinner slt_company = (Spinner) findViewById(R.id.select_company_id);
                if (company_list_str != null) {
                    System.out.println(company_list_str);
                    try {
//                        System.out.println(msg.obj);
                        JSONArray jsonArray = new JSONArray(String.valueOf(company_list_str));
                        company_list = jsonArray;
                        System.out.println(company_list.toString());
                        String[] names = new String[company_list.length()];
                        for (int i = 0; i < company_list.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) company_list.opt(i);
                            System.out.println(jsonObject2.get("name"));
                            names[i] = (String) jsonObject2.get("name");
                            Integer garage_id = (Integer) jsonObject2.get("id");
                            if (i == company_list.length() - 1) {
                                paper.setGarage_id(garage_id);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PaperEnterActivity.this, android.R.layout.simple_spinner_item, names);
                        //绑定 Adapter到控件
                        slt_company.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(msg);
            }
        };
    }

    /**
     * fetch garage http
     */
    public void fetch_garage() {
        System.out.println(Const.URL_GARAGE_LIST);
        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest httpRequest = new HttpGet(Const.URL_GARAGE_LIST);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("finish get success");
                garage_list_str = EntityUtils.toString(httpResponse.getEntity());
            } else {
                System.out.println("finish get fail");
                garage_list_str = "失败";
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * fetch company http
     */
    public void fetch_company() {
        System.out.println(Const.URL_COMPANY_LIST);
        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest httpRequest = new HttpGet(Const.URL_COMPANY_LIST);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("finish get success");
                company_list_str = EntityUtils.toString(httpResponse.getEntity());
            } else {
                System.out.println("finish get fail");
                company_list_str = "失败";
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * post paper http
     */
    public void post_paper() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(Const.URL_PAPER_SAVE);
        HttpResponse httpResponse;
        JSONObject json = new JSONObject();
        try {
            json.put("paper_code", paper.getPape_code());
            json.put("car_license", paper.getCar_license());
            json.put("report_code", paper.getReport_code());
            json.put("garage_id", paper.getGarage_id());
            json.put("company_id", paper.getCompany_id());
            json.put("type", paper.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = new StringEntity(json.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpRequest.setEntity(entity);
        try {
            httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println("finish post paper");
            } else {
                System.out.println("finish get fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
