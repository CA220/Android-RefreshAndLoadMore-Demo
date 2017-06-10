package com.takwolf.android.refreshandloadmore.demo.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.takwolf.android.refreshandloadmore.demo.R;
import com.takwolf.android.refreshandloadmore.demo.model.illust.IllustClient;
import com.takwolf.android.refreshandloadmore.demo.ui.adapter.IllustListAdapter2;
import com.takwolf.android.refreshandloadmore.demo.ui.listener.NavigationFinishClickListener;
import com.takwolf.android.refreshandloadmore.demo.ui.viewholder.LoadMoreFooter;
import com.takwolf.android.refreshandloadmore.demo.ui.widget.ListView;
import com.takwolf.android.refreshandloadmore.demo.util.HandlerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AbsListNotFullDemoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreFooter.OnLoadMoreListener {

    private static final int PAGE_SIZE = 1;
    private static final int TOTAL_COUNT = 8;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.list_view)
    ListView listView;

    private LoadMoreFooter loadMoreFooter;
    private IllustListAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ButterKnife.bind(this);

        toolbar.setTitle("ListView 第一页不足一屏");
        toolbar.setNavigationOnClickListener(new NavigationFinishClickListener(this));

        loadMoreFooter = new LoadMoreFooter(this, listView, this);

        adapter = new IllustListAdapter2(this);
        listView.setAdapter(adapter);

        refreshLayout.setColorSchemeResources(R.color.color_accent);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        HandlerUtils.handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                adapter.getIllustList().clear();
                adapter.getIllustList().addAll(IllustClient.buildIllustList(PAGE_SIZE));
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                loadMoreFooter.setState(LoadMoreFooter.STATE_ENDLESS);
            }

        }, 1000);
    }

    @Override
    public void onLoadMore() {
        HandlerUtils.handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                adapter.getIllustList().addAll(IllustClient.buildIllustList(PAGE_SIZE));
                adapter.notifyDataSetChanged();
                loadMoreFooter.setState(adapter.getCount() >= TOTAL_COUNT ? LoadMoreFooter.STATE_FINISHED : LoadMoreFooter.STATE_ENDLESS);
            }

        }, 1000);
    }

}
