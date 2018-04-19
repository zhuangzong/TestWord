package com.zz.testword;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zz.testword.util.FileUtil;
import com.zz.testword.util.WordUtil;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText et;
    private TextView tv;
    private Button bt;
    private ListView lv;
    private String string="";
    private String filePath;
    private String url;
    private int width1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et= (EditText) findViewById(R.id.et_main);
        tv= (TextView) findViewById(R.id.tv_main_total);
        bt= (Button) findViewById(R.id.bt_main);
        lv= (ListView) findViewById(R.id.lv_main);
        WindowManager wm1 = this.getWindowManager();
        width1= wm1.getDefaultDisplay().getWidth();
        String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();// 获取sdcard的根路径
        filePath= sdpath + File.separator + "le" + File.separator;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        FileUtil.copyFilesFromRaw(this,R.raw.asd,"asd.docx",filePath);
        final WordUtil wu = new WordUtil(filePath+"asd.docx");
        try {
            string=FileUtil.getString(new FileInputStream(new File(wu.htmlPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("qwe",et.getText().toString());
                if (et.getText().toString().length()>0){
                    String ss=string.replace(et.getText().toString(),"<font color=\"red\">"+et.getText().toString()+"</font>");
                    ByteArrayInputStream stream = new ByteArrayInputStream(("<font size=\"10\">"+ss+"</font>").getBytes());
                    FileUtil.inputstreamtofile(stream,new File(filePath+"asd2.html"));

                    final List<String> list=new ArrayList<String>();
                    List<String> list2=new ArrayList<String>();
                    final Map<Integer,Integer> map=new HashMap<Integer, Integer>();
                    String rgex = "<p>(.*?)</p>";
                    Pattern pattern = Pattern.compile(rgex);
                    Matcher matcher = pattern.matcher(ss);
                    while(matcher.find()){
                        int i = 0;
                        list.add(matcher.group(i));
                        i++;
                    }
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).contains(et.getText().toString())){
                            map.put(list2.size(),i);
                            list2.add(list.get(i));
                        }
                    }
                    tv.setText("总共查到"+list2.size()+"条");
                    MainAdapter adapter=new MainAdapter(list2,MainActivity.this);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int height=0;
                            for (int i=0;i<map.get(position)-1;i++){
                                height+=measureTextViewHeight(String.valueOf(Html.fromHtml(list.get(i))),15*width1/480,width1)+20*width1/480;
                            }
                            Intent intent=new Intent(MainActivity.this,WebViewActivity.class);
                            intent.putExtra("url",filePath+"asd2.html");
                            intent.putExtra("height",height);
                            startActivity(intent);
                        }
                    });
                }else {
                    ByteArrayInputStream stream = new ByteArrayInputStream(("<font size=\"14\">"+string+"</font>").getBytes());
                    FileUtil.inputstreamtofile(stream,new File(filePath+"asd2.html"));
                }
            }
        });
    }

    private int measureTextViewHeight(String text, int textSize, int deviceWidth) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }
}
