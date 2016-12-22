package com.safecode.andcloud;

import com.safecode.andcloud.service.LibvirtService;

/**
 * 测试通过MAC获取IP
 *
 * @author sumy
 */
public class IPAddressTest {

    public static void main(String[] args) {
        LibvirtService libvirtService = new LibvirtService();
        System.out.println(libvirtService.getIPAddressByMacAddress("00:17:5a:e6:c5:42"));
    }

}
