package com.msb.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;

/**
 * @author zhuruihong
 * @version 1.0
 * @date 2020/3/20 14:44
 * @description
 */
public class TestMyHDFS {
    private Configuration config = null;
    private FileSystem fs = null;

    @Before
    public void conn() {
        config = new Configuration(true);
        try {
//            fs = FileSystem.get(config);
            fs = FileSystem.get(URI.create("hdfs://mycluster/"), config, "zrhong");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mkdir() {
        Path path = new Path("/user/zrhong");
        try {
            fs.mkdirs(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("success");
    }

    @Test
    public void put() {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(new File("data/test.txt")));
            Path path = new Path("/user/zrhong/out.txt");
//            FSDataOutputStream out = fs.create(path);
            FSDataOutputStream out = fs.create(path, true, 1024 * 1024, (short) 2, 1024 * 1024);
            IOUtils.copyBytes(bis, out, config);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("success");
    }

    @Test
    public void delete() {
        Path path = new Path("/user/zrhong/out.txt");
        try {
            fs.delete(path, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void append() {
        Path path = new Path("/user/zrhong/out.txt");
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(new File("data/hello.txt")));
            FSDataOutputStream append = fs.append(path);
            IOUtils.copyBytes(bis, append, config);
            System.out.println("success");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void get() throws IOException {
        Path path = new Path("/user/zrhong/out.txt");
        FSDataInputStream open = fs.open(path);
        String s = null;
        while ((s = open.readLine()) != null) {
            System.out.println(s);
        }
    }


    @After
    public void close() {
        try {
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
