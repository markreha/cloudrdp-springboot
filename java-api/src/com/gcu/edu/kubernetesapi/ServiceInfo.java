package com.gcu.edu.kubernetesapi;

import java.util.Map;

public class ServiceInfo
{
	private String image;					// The Image Name
	private String name;					// The Service Name
	private String namespace;				// The Service Namespace
	private Map<String, String> labels;		// Service Labels
	private float cpus;						// The CPU's in a percent of a core, this is converted to millicores internally (multiplied by 1000 in the getter)
	private int memMBytes;					// The Memory in Mbytes
	private int replicas;					// The number of replicas to create in the Swarm
	private int targetPort;					// The Target Port inside the Container
	private int publishPort;				// The Published Port outside the Container
	private StorageInfo storageInfo;		// The Storage Info
	
	/**
	 * Non Default Constructor to specify the Service creation information.
	 * 
	 * @param image The Image Name.
	 * @param name The Swarm Service Name.
	 * @param cpus The CPU's in a percent of a core, this is converted to millicores (multiplied by 1000 in the getter).
	 * @param memMBytes The Memory in Mbytes, this is converted to bytes internally (multiplied by 1000000 in the getter).
	 * @param replicas The number of replicas to create in the Swarm.
	 * @param targetPort The Target Port inside the Container.
	 * @param publishPort The Published Port outside the Container.
	 */
	public ServiceInfo(String image, String name, String namespace, Map<String, String> labels, float cpus, int memMBytes, int replicas, int targetPort, int publishPort, StorageInfo storageInfo)
	{
		super();
		this.image = image;
		this.name = name;
		this.namespace = namespace;
		this.labels = labels;
		this.cpus = cpus;
		this.memMBytes = memMBytes;
		this.replicas = replicas;
		this.targetPort = targetPort;
		this.publishPort = publishPort;
		this.storageInfo = storageInfo;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public Map<String, String> getLabels()
	{
		return labels;
	}

	public void setLabels(Map<String, String> labels)
	{
		this.labels = labels;
	}

	public float getCpus()
	{
		return cpus * 1000;
	}

	public void setCpus(float cpus)
	{
		this.cpus = cpus;
	}

	public int getMemMBytes()
	{
		return memMBytes;
	}

	public void setMemMBytes(int memMBytes)
	{
		this.memMBytes = memMBytes;
	}

	public int getReplicas()
	{
		return replicas;
	}

	public void setReplicas(int replicas)
	{
		this.replicas = replicas;
	}

	public int getTargetPort()
	{
		return targetPort;
	}

	public void setTargetPort(int targetPort)
	{
		this.targetPort = targetPort;
	}

	public int getPublishPort()
	{
		return publishPort;
	}

	public void setPublishPort(int publishPort)
	{
		this.publishPort = publishPort;
	}

	public StorageInfo getStorageInfo()
	{
		return storageInfo;
	}

	public void setStorageInfo(StorageInfo storageInfo)
	{
		this.storageInfo = storageInfo;
	}
}
