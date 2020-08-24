package com.application.databinging;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.application.databinging.databinding.ActivityMainDemoBinding;
import com.application.databinging.event.EventClickInterface;
import com.application.databinging.model.DataModel;

import java.util.EventListener;

public class DataBingingActivityDemo extends AppCompatActivity {
    private ActivityMainDemoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*   只要通过 DataBinging 加载布局 都会对应的生成 Binding对象，比如我们生成一个 MainActivityBinding对象
         *    https://blog.csdn.net/huangxin388/article/details/77425678
         *    注意一定要把格式搞对了，才能自动生成 ActivityMainDemoBinding 对象
        */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_demo);
        // 相当于 findViewById
//        binding.mainTextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        /**
         *  在来试验 通过操作符给控件设置一些信息 （绑定基本数据类型和 String 类型）
         *  直接给 Button 设置文本和 控件是否能被点击 ，需要在  xml 的配置文件配置一些 操作符
         */
//        binding.setContent("对String类型数据的绑定");
//        binding.setEnabled(false);//设置enabled值为false
//        //给控件设置点击事件，发现其实点击无效，因为在布局文件中给cilckable属性绑定了enabled,在代码中设置enabled值为false，所以点击事件无效
//        binding.mainTv2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DataBingingActivityDemo.this, "我被点击了", Toast.LENGTH_SHORT).show();
//            }
//        });

        /**
         *  在来试验通过 绑定 Model 数据类型
         *  binding.setVariable(BR.user,user);
         */
//        DataModel data = new DataModel("绑定Model数据类型");
//        binding.setVariable(BR.dataModel,data);

        /**
         * 在来试验：绑定事件，比如给控件设置 监听
         *   1.android:onClick="@{event.click1}"
         *   2.android:onClick="@{event::click2}"
         *   3.android:onClick="@{()->event.cilck3(title4)}"
         * [注]：()->event.cilck3(title4)是lambda表达式写法，
         * 也可以写成：(view)->event.cilck3(title4),前面(view)表示onClick方法的传递的参数，
         * 如果event.click3()方法中不需要用到view参数，可以将view省略。
         *
         * 当然event.click1也可以写成(view)->event.click1(view)，将onClick(View view)的view参数传递给event.click1(view)方法。
         * 大概就这意思，以下是伪代码
         * onclick(View view){
         *     event.click1(view)
         * }
         *
         */
        binding.setTitle1("事件绑定1");
        binding.setTitle2("事件绑定2");
        binding.setTitle3("事件绑定3");
        binding.setTitle4("change ok"); // 对应 title4的值 作为cilck3(String s) 函数中的参数

        binding.setEvent(new EventClickInterface() {
            @Override
            public void click1(View v) {
                binding.setTitle1("事件1方法调用");
            }

            @Override
            public void click2(View v) {
                binding.setTitle2("事件2方法调用");
            }

            @Override
            public void cilck3(String s) {
                binding.setTitle3(s);
            }
        });
    }
}
