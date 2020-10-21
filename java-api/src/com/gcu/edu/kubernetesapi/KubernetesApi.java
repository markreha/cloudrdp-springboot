package com.gcu.edu.kubernetesapi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.Map;

import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class KubernetesApi
{
	private String kubeConfigPath;
	private KubernetesClient client;
	
	/**
	 * Initialize the underlying connection to the Kubernetes Client API and connect to the Cluster.
	 * 
	 * @param kubeConfigPath Path to the Kube Config File
	 * @return An instance of the Kubernetes Client API
	 * @throws Exception
	 */
	public void initialize(String kubeConfigPath) throws Exception
	{
		// Save the Kube Config file path
		this.kubeConfigPath = kubeConfigPath;
		
	    // Read the Kube Config file contents
	    StringBuilder contentBuilder = new StringBuilder();
       	BufferedReader br = new BufferedReader(new FileReader(this.kubeConfigPath));
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) 
            contentBuilder.append(sCurrentLine).append("\n");
        br.close();
 	    
	    // Get an instance of the Kubernetes Client setting its configuration from the Kube File contents
        Config config = Config.fromKubeconfig(contentBuilder.toString());
	    this.client = new DefaultKubernetesClient(config);	    
	}

	/**
	 * Get a list of all Pods from desired or all Name Spaces.
	 * 
	 * @param nameSpace Name Space to search, if null returns from all Name Spaces
	 * @return a PodList
	 */
	public PodList getAllPods(String nameSpace)
	{
	    // Call the underlying client API to all the Pods from all Names Spaces
		if(nameSpace == null)
			return client.pods().inAnyNamespace().list();
		else
			return client.pods().inNamespace(nameSpace).list();
	}
	
	/**
	 * Get a Pods log file.
	 * 
	 * @param pod Pod name to get logs for
	 * @param namespace Name Space that the Pod is in
	 * @return String for Logs
	 */
	public String getPodLog(String pod, String namespace)
	{
		return client.pods().inNamespace(namespace).withName(pod).getLog();
	}

	/**
	 * Get a list of all Nodes.
	 * 
	 * @return a NodeList
	 */
	public NodeList getAllNodes()
	{
	    // Call the underlying client API to all the Nodes
	    return client.nodes().list();
	}

	/**
	 * Get a list of all Deployments.
	 * 
	 * @return a DeploymentList
	 */
	public DeploymentList getAllDeployments()
	{
	    // Call the underlying client API to all the Deployments
		return client.apps().deployments().list();
	}

	/**
	 * Get a list of all Services from desired or all Name spaces.
	 * 
	 * @param nameSpace Name Space to search, if null returns from all Name Spaces
	 * @return a ServicesList
	 */
	public ServiceList getAllServices(String nameSpace)
	{
	    // Call the underlying client API to all the Nodes
		if(nameSpace == null)
			return client.services().inAnyNamespace().list();
		else
			return client.services().inNamespace(nameSpace).list();
	}
	
	/**
	 * Create a Service using the specified info.
	 *  
	 *  NOTE on Limits and Request: 
	 *  	Requests are what the container is guaranteed to get. If a container Requests 
	 *  	a resource, Kubernetes will only schedule it on a node that can give it that resource.
	 *  	Limits, make sure a container never goes above a certain value. The container 
	 *  	is only allowed to go up to the limit, and then it is restricted.
	 *  	CPU is specified in millicores and Memory is specified in Bytes.
	 *  
	 * 
	 * @param info Service Specification.
	 * @return Service created.
	 */
	public Service createService(ServiceInfo info)
	{
		// Create deployment
		Deployment deployment = new DeploymentBuilder()
				   .withNewMetadata()
				      .withName(info.getName())
				      .addToLabels(info.getLabels())
				   .endMetadata()
				   .withNewSpec()
				      .withReplicas(info.getReplicas())
				      .withNewTemplate()
				        .withNewMetadata()
				        .addToLabels("app", info.getName())
				        .endMetadata()
				        .withNewSpec()
				          .addNewContainer()
				             .withName(info.getName())
				             .withImage(info.getImage())
				             .withNewResources()
				             	.addToLimits(Collections.singletonMap("cpu", new Quantity(info.getCpus() + "m")))
				             	.addToLimits(Collections.singletonMap("memory", new Quantity(info.getMemMBytes() + "Mi")))
				             	.addToRequests(Collections.singletonMap("cpu", new Quantity(info.getCpus() + "m")))
				             	.addToRequests(Collections.singletonMap("memory", new Quantity(info.getMemMBytes() + "Mi")))
				             .endResources()
				             .addNewVolumeMount()
				             	.withName(info.getStorageInfo().getName())				             	
				             	.withMountPath(info.getStorageInfo().getPodPath())
				             	.withSubPath(info.getStorageInfo().getPvSubPath())
				             .endVolumeMount()
				          .endContainer()
				          .addNewVolume()
				          	.withName(info.getStorageInfo().getName())
				          	.withNewPersistentVolumeClaim(info.getStorageInfo().getPvClaimName(), false)
				          .endVolume()
				        .endSpec()
				      .endTemplate()
				      .withNewSelector()
				        .addToMatchLabels("app", info.getName())
				      .endSelector()
				   .endSpec()
				 .build();
		client.apps().deployments().inNamespace(info.getNamespace()).create(deployment);
				
		// Create service
		Service createdSvc = client.services().inNamespace("default").createOrReplaceWithNew()
			    .withNewMetadata()
			    	.withName(info.getName() + "-service")
			    	.addToLabels(info.getLabels())
			    .endMetadata()
			    .withNewSpec()
			    	.addToSelector("app", info.getName())
			    	.withType("NodePort")
			    	.addNewPort()
			    		.withName(info.getTargetPort() + "-" + info.getPublishPort()).withProtocol("TCP").withPort(info.getPublishPort()).withNewTargetPort(info.getTargetPort())
			    	.endPort()
			    .endSpec()
			    .withNewStatus()
			    .endStatus()
			    .done();
		return createdSvc;
	}
	
	/**
	 * Find Services based on matching Labels.
	 * 
	 * @param namespace Name Space to search, if null returns from all Name Spaces
	 * @param labels Labels to search for
	 * @return a ServicesList
	 */
	public ServiceList findServices(String namespace, Map<String, String> labels)
	{
	    // Call the underlying client API to all the Services with the matching Labels
		if(namespace == null)
			return client.services().inAnyNamespace().withLabels(labels).list();
		else
			return client.services().inNamespace(namespace).withLabels(labels).list();
	}
	
	/**
	 * Delete a Service using the specified info.
	 * 
	 * @param serviceName Service to delete
	 * @param namespace Service in the namepace to delete
	 * @return True if OK else False if deletion failed
	 */
	public boolean deleteService(String serviceName, String namespace)
	{
		// Delete Service
		Boolean isDeleted = client.services().inNamespace(namespace).withName(serviceName + "-service").delete();
		if(!isDeleted.booleanValue())
			return false;
		
		// Delete Deployment
		return client.apps().deployments().inNamespace(namespace).withName(serviceName).delete();
	}
	
	/**
	 * Cleanup the connections etc.
	 * 
	 */	
	public void cleanup()
	{
		// Call underlying client API to close things
	    client.close();
	}
}
