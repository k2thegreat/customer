package com.tp.customer.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tp.customer.controller.Product;
import com.tp.customer.entity.Customer;
import com.tp.customer.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RestTemplate restTemplate;

    @Qualifier("eurekaClient")
    @Autowired
    EurekaClient client;

    public List<Customer> getCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }


    public Product getProduct(int custId) {
        int prodId = customerRepository.findById(custId).map(Customer::getProdId).orElse(0);
        Application application = client.getApplication("PRODUCT-SERVICE");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/product/" + prodId;
        System.out.println("URL" + url);
        String loadBalancedUrl = "http://PRODUCT-SERVICE" + "/product/" + prodId;
        return restTemplate.getForObject(loadBalancedUrl, Product.class);
    }
}
