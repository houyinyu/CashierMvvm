package com.hyy.mvvm.app;



import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.data.DemoRepository;
import com.hyy.mvvm.data.LocalDataSource;
import com.hyy.mvvm.data.LocalDataSourceImpl;
import com.hyy.mvvm.http.HttpDataSource;
import com.hyy.mvvm.http.HttpDataSourceImpl;

/**
 * 注入全局的数据仓库，可以考虑使用Dagger2。（根据项目实际情况搭建，千万不要为了架构而架构）
 * Created by goldze on 2019/3/26.
 */
public class Injection {
    public static DemoRepository provideDemoRepository(BaseActivity activity) {
        //网络数据源
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(activity);
        //本地数据源
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();
        //两条分支组成一个数据仓库
        return DemoRepository.getInstance(httpDataSource, localDataSource);
    }
}
