package pers.xz.mybatis;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import pers.xz.mybatis.dao.GoodsDAO;
import pers.xz.mybatis.dto.GoodsDTO;
import pers.xz.mybatis.entity.Goods;
import pers.xz.mybatis.entity.GoodsDetail;
import pers.xz.mybatis.utils.MybatisUtils;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.util.*;

// JUNIT单元测试类
public class MyBatisTest {
    @Test
    public void testSqlSessionFactory() throws IOException {
        // 利用Reader加载classpath下的mybatis-config.xml核心配置文件
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        // 初始化SqlSessionFactory对象，同时解析mybatis-config.xml文件
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        System.out.println("SessionFactory加载成功");
        SqlSession sqlSession = null;
        try {
            // 创建SqlSession对象，SqlSession是JDBC的扩展类，用于与数据库交互
            sqlSession = sqlSessionFactory.openSession();
            // 创建数据库连接(测试用，实际开发中并不使用)
            Connection connection = sqlSession.getConnection();
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                // 如果type="POOLED"，代表使用连接池，close则是将连接回收到连接池中
                // 如果type="UNPOOLED"，代表直连，close则会调用Connection.close()方法关闭连接
                sqlSession.close();
            }
        }
    }

    @Test
    public void testMybatisUtils() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MybatisUtils.openSession();
            Connection connection = sqlSession.getConnection();
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MybatisUtils.closeSession(sqlSession);
        }
    }

    @Test
    public void testSelectAll() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            List<Goods> list = session.selectList("goods.selectAll");
            for (Goods g : list) {
                System.out.println(g.getTitle());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testSelectById() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            Goods goods = session.selectOne("goods.selectById", 1603);
            System.out.println(goods.getTitle());
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testSelectByPriceRange() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            Map param = new HashMap();
            param.put("min", 100);
            param.put("max", 500);
            param.put("limit", 10);
            List<Goods> list = session.selectList("selectByPriceRange", param);
            for (Goods g : list) {
                System.out.println(g.getTitle() + ":" + g.getCurrentPrice());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testSelectGoodsMap() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            List<Map> list = session.selectList("goods.selectGoodsMap");
            for (Map map : list) {
                System.out.println(map);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testSelectGoodsDTO() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            List<GoodsDTO> list = session.selectList("goods.selectGoodsDTO");
            for (GoodsDTO g : list) {
                System.out.println(g.getGoods().getTitle());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testInsert() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            Goods goods = new Goods();
            goods.setTitle("测试商品");
            goods.setSubTitle("测试子标题");
            goods.setOriginalCost(200f);
            goods.setCurrentPrice(100f);
            goods.setDiscount(0.5f);
            goods.setIsFreeDelivery(1);
            goods.setCategoryId(43);
            // insert()方法返回值代表本次成功插入的记录总数
            int num = session.insert("goods.insert", goods);
            session.commit(); // 提交事务数据
            System.out.println(goods.getGoodsId());
        } catch (Exception e) {
            if (session != null) {
                session.rollback(); // 回滚事务
            }
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testUpdate() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            Goods goods = session.selectOne("goods.selectById", 739);
            goods.setTitle("更新测试商品");
            int num = session.update("goods.update", goods);
            session.commit(); // 提交事务数据
        } catch (Exception e) {
            if (session != null) {
                session.rollback(); // 回滚事务
            }
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testDelete() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            int num = session.delete("goods.delete", 739);
            System.out.println(num);
            session.commit(); // 提交事务数据
        } catch (Exception e) {
            if (session != null) {
                session.rollback(); // 回滚事务
            }
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testDynamicSQL() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            Map parm = new HashMap();
            parm.put("categoryId", 44);
            parm.put("currentPrice", 500);
            // 查询条件
            List<Goods> list = session.selectList("goods.dynamicSQL", parm);
            for (Goods g : list) {
                System.out.println(g.getTitle() + ":" + g.getCategoryId() + ":" + g.getCurrentPrice());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }
    @Test
    public void testLevel1() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            // 一级缓存被默认开启，生存周期属于session
            Goods goods = session.selectOne("goods.selectById", 1603);
            Goods goods1 = session.selectOne("goods.selectById", 1603);
            System.out.println(goods.hashCode() + ":" + goods1.hashCode());
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
        try {
            session = MybatisUtils.openSession();
            Goods goods = session.selectOne("goods.selectById", 1603);
            session.commit();  // commit提交时对该namespace缓存强制清空
            Goods goods1 = session.selectOne("goods.selectById", 1603);
            System.out.println(goods.hashCode() + ":" + goods1.hashCode());
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testLevel2() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            // 一级缓存被默认开启，生存周期属于session
            Goods goods = session.selectOne("goods.selectById", 1603);
            System.out.println(goods.hashCode());
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
        try {
            session = MybatisUtils.openSession();
            Goods goods = session.selectOne("goods.selectById", 1603);
            session.commit();  // commit提交时对该namespace缓存强制清空
            System.out.println(goods.hashCode());
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testOneToMany() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            List<Goods> list = session.selectList("goods.selectOneToMany");
            for (Goods goods : list) {
                System.out.println(goods.getTitle() + ":" + goods.getGoodsDetails().size());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testManyToOne() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            List<GoodsDetail> list = session.selectList("goodsDetail.selectManyToOne");
            for (GoodsDetail gd : list) {
                System.out.println(gd.getGdPicUrl() + ":" + gd.getGoods().getTitle());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testSelectPage() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            // startPage方法会自动将下一次查询进行分页, 查询第二页，每页十条数据
            PageHelper.startPage(2, 10);
            Page<Goods> page = (Page) session.selectList("goods.selectPage");
            System.out.println("总页数" + page.getPages());
            System.out.println("总记录数" + page.getTotal());
            System.out.println("开始行号" + page.getStartRow());
            System.out.println("结束行号" + page.getEndRow());
            System.out.println("当前页码" + page.getPageNum());
            List<Goods> data = page.getResult();  // 当页数据
            for (Goods g : data) {
                System.out.println(g.getTitle());
            }
            System.out.println("");
        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testBatchInsert() throws Exception {
        SqlSession session = null;
        try {
            long st = new Date().getTime();
            session = MybatisUtils.openSession();
            List list = new ArrayList();
            for (int i = 0; i < 100; i++) {
                Goods goods = new Goods();
                goods.setTitle("测试商品");
                goods.setSubTitle("测试子标题");
                goods.setOriginalCost(200f);
                goods.setCurrentPrice(100f);
                goods.setDiscount(0.5f);
                goods.setIsFreeDelivery(1);
                goods.setCategoryId(43);
                // insert方法返回值代表本次插入的记录总数
                list.add(goods);
            }
            session.insert("goods.batchInsert", list);
            session.commit();
            long et = new Date().getTime();
            System.out.println("执行时间：" + (et - st) + "毫秒");
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

    @Test
    public void testBatchDelete() throws Exception {
        SqlSession session = null;
        try {
            long st = new Date().getTime();
            session = MybatisUtils.openSession();
            List list = new ArrayList();
            list.add(1920);
            list.add(1921);
            list.add(1922);
            session.delete("goods.batchDelete", list);
            session.commit();
            long et = new Date().getTime();
            System.out.println("执行时间：" + (et - st) + "毫秒");
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }
    @Test
    public void testCom() throws Exception {
        SqlSession session = null;
        try {
            session = MybatisUtils.openSession();
            GoodsDAO goodsDAO = session.getMapper(GoodsDAO.class);
            List<Goods> list = goodsDAO.selectByPriceRange(100f, 200f, 10);
            System.out.println(list.size());

//            GoodsDAO goodsDAO = session.getMapper(GoodsDAO.class);
//            int num = goodsDAO.insert(goods);
//            session.commit();

//            GoodsDAO goodsDAO = session.getMapper(GoodsDAO.class);
//            List<GoodsDTO> list = goodsDAO.selectAll();

        } catch (Exception e) {
            throw e;
        } finally {
            MybatisUtils.closeSession(session);
        }
    }

}
