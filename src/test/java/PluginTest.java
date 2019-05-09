import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xt.mybatis.bean.Department;
import com.xt.mybatis.bean.Employee;
import com.xt.mybatis.bean.OraclePage;
import com.xt.mybatis.bean.Status;
import com.xt.mybatis.mapper.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;


public class PluginTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    /**
     * 插件原理
     * 在四大对象创建的时候
     * 1、每个创建出来的对象不是直接返回的，而是
     * 		interceptorChain.pluginAll(parameterHandler);
     * 2、获取到所有的Interceptor（拦截器）（插件需要实现的接口）；
     * 		调用interceptor.plugin(target);返回target包装后的对象
     * 3、插件机制，我们可以使用插件为目标对象创建一个代理对象；AOP（面向切面）
     * 		我们的插件可以为四大对象创建出代理对象；
     * 		代理对象就可以拦截到四大对象的每一个执行；
     *
     public Object pluginAll(Object target) {
     for (Interceptor interceptor : interceptors) {
     target = interceptor.plugin(target);
     }
     return target;
     }

     */
    /**
     * 插件编写：
     * 1、编写Interceptor的实现类
     * 2、使用@Intercepts注解完成插件签名
     * 3、将写好的插件注册到全局配置文件中
     *
     */
    @Test
    public void testFirstPlugin () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee emp = mapper.getById(2);
            System.out.println(emp);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testPage () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            Page<Object> page = PageHelper.startPage(4, 1);

            List<Employee> emps = mapper.selectAll();

            //传入要连续显示多少页
            PageInfo<Employee> info = new PageInfo<>(emps, 3);

            System.out.println(emps);

            /*System.out.println("当前页码：" + page.getPageNum());
            System.out.println("总记录数: " + page.getTotal());
            System.out.println("每页记录数：" + page.getPageSize());
            System.out.println("总页码：" + page.getPages());*/

            System.out.println("当前页码："+info.getPageNum());
            System.out.println("总记录数："+info.getTotal());
            System.out.println("每页的记录数："+info.getPageSize());
            System.out.println("总页码："+info.getPages());
            System.out.println("是否第一页："+info.isIsFirstPage());
            System.out.println("连续显示的页码：");
            int[] nums = info.getNavigatepageNums();
            for (int i = 0; i < nums.length; i++) {
                System.out.println(nums[i]);
            }

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testBatch () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        //可以执行批量操作的sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        Instant start = Instant.now();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            for (int i=1; i<=1000; i++) {
                mapper.addEmp(new Employee("aa" + i, "aa@163.com", "1"));
            }
            sqlSession.commit();
            Instant end = Instant.now();
            //批量：（预编译sql一次==>设置参数===>10000次===>执行（1次））
            //Parameters: 616c1(String), b(String), 1(String)==>4598
            //非批量：（预编译sql=设置参数=执行）==》10000    10200
            System.out.println("执行时长:" + Duration.between(start, end).toMillis());

        } finally {
            sqlSession.close();
        }
    }

    /**
     * oracle分页：
     * 		借助rownum：行号；子查询；
     * 存储过程包装分页逻辑
     * @throws IOException
     */
    @Test
    public void testProcedure() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            OraclePage page = new OraclePage();
            page.setStart(1);
            page.setEnd(5);
            mapper.getPageByProcedure(page);

            System.out.println("总记录数："+page.getCount());
            System.out.println("查出的数据："+page.getEmps().size());
            System.out.println("查出的数据："+page.getEmps());
        }finally{
            openSession.close();
        }
    }



    @Test
    public void testStatus () throws IOException {

        Status logout = Status.LOGOUT;
        System.out.println("枚举的索引：" + logout.ordinal());
        System.out.println("枚举的名字：" + logout.name());
//        System.out.println("枚举的状态码：" + logout.getCode());
//        System.out.println("枚举的提示消息：" + logout.getMsg());

    }

    /**
     * 默认mybatis在处理枚举对象的时候保存的是枚举的名字：EnumTypeHandler
     * 改变使用：EnumOrdinalTypeHandler：
     * @throws IOException
     */
    @Test
    public void testEnum () throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee emp = new Employee(null, "aaa", "aaa@163.com", "1");
            mapper.addEmp(emp);
            openSession.commit();
            System.out.println(emp.getId());
        }finally{
            openSession.close();
        }
    }
}
