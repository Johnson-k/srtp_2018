package com.briup.client;

import java.util.Collection;

import com.briup.bean.Environment;

public interface Client {
	void send(Collection<Environment> collection);
}
