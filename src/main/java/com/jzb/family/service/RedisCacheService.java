package com.jzb.family.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.com.caucho.hessian.io.HessianInput;
import com.alibaba.com.caucho.hessian.io.HessianOutput;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by MADEXIN138 on 2015/3/27.
 */
public class RedisCacheService {
	private static final Logger logger = LoggerFactory
			.getLogger(RedisCacheService.class);
    private static final String charset_name = "ISO-8859-1";
    private JedisPool jedisPool;

    /**
     * 获得对象
     *
     * @param key
     * @return
     * @throws Exception
     */
    public Object get(byte[] key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] value = jedis.get(key);
            return value == null ? null : toObject(value);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
    /**
     * 获得对象
     *
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] getByte(String key) throws Exception {
    	Jedis jedis = null;
    	try {
    		jedis = jedisPool.getResource();
    		byte[] value = jedis.get(key.getBytes(charset_name));
    		return value == null ? null : value;
    	} finally {
    		if (jedis != null) {
    			jedisPool.returnResource(jedis);
    		}
    	}
    }

    /**
     * 获得对象（重载）
     *
     * @param key
     * @return
     * @throws Exception
     */
    public Object get(String key) throws Exception {
        return get(key.getBytes(charset_name));
    }

    /**
     * 新增元素
     *
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public String put(String key, int seconds, Object value) throws Exception {
        return put(key.getBytes(charset_name), seconds, value);
    }

    /**
     * 新增元素(重载)
     *
     * @param key
     * @param value
     * @return
     */
    public String put(byte[] key, int seconds, Object value) throws Exception {
        Jedis jedis = null;
        try {
            if (key == null || key.length == 0 || value == null) {
                throw new Exception("Key和Value不能为空.");
            }
            long start = System.currentTimeMillis();
            jedis = jedisPool.getResource();
            logger.info("redis jedisPool.getResource() time:"+(System.currentTimeMillis()-start));
            if(seconds>0){
            	return jedis.setex(key, seconds, toBytes(value));
            }else{
            	return jedis.set(key,toBytes(value));
            }
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
    /**
     * 新增元素(重载)
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public String putByte(String key, int seconds, byte[] value) throws Exception {
    	Jedis jedis = null;
    	try {
    		if (key == null || key.isEmpty() || value == null) {
    			throw new Exception("Key和Value不能为空.");
    		}
    		long start = System.currentTimeMillis();
    		jedis = jedisPool.getResource();
    		logger.info("redis jedisPool.getResource() time:"+(System.currentTimeMillis()-start));
    		if(seconds>0){
    			return jedis.setex(key.getBytes(charset_name), seconds, value);
    		}else{
    			return jedis.set(key.getBytes(charset_name),value);
    		}
    	} finally {
    		if (jedis != null) {
    			jedisPool.returnResource(jedis);
    		}
    	}
    }

    /**
     * 删除元素
     *
     * @param key
     * @throws Exception
     */
    public long delete(String key) throws Exception {
        return delete(key.getBytes(charset_name));
    }

    /**
     * 删除元素（重载）
     *
     * @param key
     * @throws Exception
     */
    public long delete(byte[]... keys) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(keys);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

//    private byte[] toBytes(Object object) throws IOException {
//        ObjectOutputStream oos = null;
//        ByteArrayOutputStream baos = null;
//        long s1= 0;
//        try {
//            // 序列匄1�7
//            baos = new ByteArrayOutputStream();
//            oos = new ObjectOutputStream(baos);
//            s1= System.currentTimeMillis();
//            oos.writeObject(object);
//            return baos.toByteArray();
//        } finally {
//            try {
//                if (oos != null) {
//                    oos.close();
//                }
//                if (oos != null) {
//                    baos.close();
//                }
//            } catch (IOException e) {
//                //igonre
//            }
//            logger.info("toBytes baos size :{},time:{} ",baos.size(),(System.currentTimeMillis()-s1));
//        }
//    }
//
//    private Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
//        ObjectInputStream ois = null;
//        long start=0;
//        try {
//        	 start = System.currentTimeMillis();
//            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
//            return ois.readObject();
//        } finally {
//            try {
//                if (ois != null) {
//                    ois.close();
//                }
//            } catch (IOException e) {
//                //igonre
//            }
//            logger.info("redis toObject() time:{}",(System.currentTimeMillis()-start));
//        }
//    }
    //以下是不抛异常方泄1�7
    /**
     * 获得对象 不抛异常
     *
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
	public <T>T getNoThrow(String key,Class<T> clazz) {
    	try {
    		return (T)get(key.getBytes(charset_name));
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    	}
    	return null;
    }
    /**
     * 获得对象列表 不抛异常
     *
     * @param key
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
	public <T> List<T> getListNoThrow(String key,Class<T> clazz) {
    	try {
    		return (List<T>)get(key.getBytes(charset_name));
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    	}
    	return null;
    }

    /**
     * 新增元素 不抛异常
     *
     * @param key
     * @param value
     * @return String
     */
    public String putNoThrow(String key, int seconds, Object value){
    	try {
    		return put(key.getBytes(charset_name), seconds, value);
    	}catch(Exception e){
    		logger.error(e.getMessage(), e);
    	}
    	return null;
    }

    /**
     * 序列匄1�7
     * @param object
     * @return
     * @throws Exception
     */
	public byte[] toBytes(final Object object) throws Exception {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final HessianOutput hessianOutput = new HessianOutput(
				byteArrayOutputStream);
		try {
			hessianOutput.writeObject(object);
			return byteArrayOutputStream.toByteArray();
		} finally {
			try {
				hessianOutput.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public Object toObject(final byte[] bytes) throws Exception {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				bytes);
		final HessianInput hessianInput = new HessianInput(byteArrayInputStream);
		try {
			return hessianInput.readObject();
		} finally {
			hessianInput.close();
		}
	}
    
    /**
     * 模糊查询key
     *
     * @param key
     * @throws Exception
     */
    public Set<byte[]> keys(String key) throws Exception {
        return keys(key.getBytes(charset_name));
    }

    /**
     * 模糊查询key（重载）
     *
     * @param key
     * @throws Exception
     */
    public Set<byte[]> keys(byte[] key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(key);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }

    }
}
