package sing.bubblelayout.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import sing.BubbleLayout;
import sing.BubblePopupHelper;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<String> list;

    private PopupWindow popupWindow;
    private BubbleLayout bubbleLayout;

    private int[] recyclerViewLocation = new int[2];// recyclerView的坐标
    private int[] location = new int[2];//点击的Item的坐标

    private int screenY = 0;// 屏幕高度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenY = metric.heightPixels;

        bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.layout_sample_popup, null);
        bubbleLayout.measure(0, 0);// 猜猜有什么作用
        popupWindow = BubblePopupHelper.create(this, bubbleLayout);

        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("测试数据 " + i);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void click(View v, int position) {
        recyclerView.scrollToPosition(position);

        v.getLocationInWindow(location);
        recyclerView.getLocationInWindow(recyclerViewLocation);

        // 三角距离item顶端的距离
        float hornToItemTop = v.getHeight()/2 - bubbleLayout.getArrowHeight()/2;
        // 弹出框高出Item的距离
        int higherDistance = (bubbleLayout.getMeasuredHeight() - v.getHeight()) / 2;
        // 弹出框的y轴顶部坐标
        int bubbleLayoutTop = location[1] - higherDistance;
        // 弹出框的y轴底部部坐标
        int bubbleLayoutBottom = bubbleLayoutTop + bubbleLayout.getMeasuredHeight();

        int aaa = location[1]- higherDistance;
        if (aaa < recyclerViewLocation[1]){// 弹出框超过了顶部
            bubbleLayoutTop = recyclerViewLocation[1];
        }

        if (bubbleLayoutBottom > screenY){
            bubbleLayoutTop = screenY - bubbleLayout.getMeasuredHeight();
        }

        if (location[1] < recyclerViewLocation[1]){
            location[1] = recyclerViewLocation[1];
        }
        if (location[1] > screenY - v.getHeight()){
            location[1] = screenY - v.getHeight();
        }

        bubbleLayout.setArrowPosition(location[1] - bubbleLayoutTop + hornToItemTop);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, v.getWidth(), bubbleLayoutTop);
    }
}