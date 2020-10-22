package com.gcu.edu.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.gcu.edu.kubernetesapi.KubernetesApi;
import com.gcu.edu.kubernetesapi.ServiceInfo;
import com.gcu.edu.kubernetesapi.StorageInfo;

import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;

public class Play
{
	// Various Kubernetes Configuration Files for testing
	//  Get these from the Master Node at $HOME/.kube/config
//	static String CONFIG = "./config";										// Active Kubernetes Config 1
	static String CONFIG = "./config-pi";									// Active Kubernetes Config 2
	static String USER_ID = "mreha";										// User ID for testing
	static Scanner reader = new Scanner(System.in);							// Input Scanner used for the Menu
	static KubernetesApi api = new KubernetesApi();							// GCU Kubernetes Client API
	
	/**
	 * Test Kubernetes Java API using the Fabric8io Kubernetes Java Client API
	 * See documentation at https://github.com/fabric8io/kubernetes-client
	 * 
	 * @param args
	 * @throws IOException
	 * @throws ApiException
	 */
	public static void main(String[] args) throws Exception, IOException
	{
	    // Set the active Kube Config file and initialize Kubernetes Client
	    String kubeConfigPath = Play.CONFIG;
	    
		// Initialize and Wait for commands
		System.out.println("GCU Kubernetes Client Test v1");
		System.out.println("=======> Intitializing.........");		
		api.initialize(kubeConfigPath);
		int cmd = -1;
		do
		{
			// Display the Menu
			System.out.println("");
			System.out.println("");
			System.out.println("=======================================");
			System.out.println("===== Test Kubernetes Client Menu =====");
			System.out.println("=======================================");
			System.out.println("");
			System.out.println("      0 to Quit");
			System.out.println("      1 to List Pods");
			System.out.println("      2 to List Nodes");
			System.out.println("      3 to List Deployments");
			System.out.println("      4 to List Services");
			System.out.println("      5 to Create a Nginx Service");
			System.out.println("      6 to Create a Tomcat 8.5 Service");
			System.out.println("      7 to Delete a Service");
			System.out.println("      8 to Get a Pods Log file");
			System.out.println("      9 to Search for Services");
			System.out.print("  Enter a Command: ");
			
			// Get Menu Option from the user and run the specified command
			cmd = reader.nextInt();
			switch (cmd)
			{
				case 1:
					listAllPods();
					break;
				case 2:
					listAllNodes();
					break;
				case 3:
					listAllDeployments();
					break;
				case 4:
					listAllServices();
					break;
				case 5:
					createNginxService();
					break;
				case 6:
					createTomcatService();
					break;
				case 7:
					deleteService();
					break;
				case 8:
					getPodLogs();
					break;
				case 9:
					findServices();
					break;
			}
		}while (cmd != 0);
	    
	    // Cleanup
	    api.cleanup();
	    reader.close();
	    System.out.println("=======> Done");
	}
	
	// ***** Private Helper Functions *****
	
	private static boolean listAllPods()
	{
		// Call the Client API to get a list of all Pods
		PodList podList = api.getAllPods(null);
		if(podList.getItems().size() == 0)
		{
			System.out.println("=======> No Pods available");
			return false;
		}
	    for (Pod item : podList.getItems()) 
	    {
	    	System.out.println("=======> " + " Pod Name of " + item.getMetadata().getName() 
	    									+ " in Name Space " + item.getMetadata().getNamespace() 
	    									+ " with a status of " + item.getStatus().getPhase());
	    }
	    return true;
	}

	private static boolean getPodLogs()
	{
		// Display all Pods and prompt the user for which POd from Name Space to get logs for
		if(!listAllPods())
			return false;
		System.out.print("Enter Pod to get: ");
		String pod = reader.next();
		System.out.print("From Namespace: ");
		String namespace = reader.next();
		
		// Call the Client API to get a Pods log files
		String logs = api.getPodLog(pod, namespace);
		System.out.println("=======> Pod logs: \n" + logs);
		return true;
	}

	private static boolean listAllNodes()
	{
		// Call the Client API to get a list of all Nodes
		NodeList nodeList = api.getAllNodes();
		if(nodeList.getItems().size() == 0)
		{
			System.out.println("=======> No Nodes available");
			return false;
		}
	    for (Node item : nodeList.getItems()) 
	    {
	    	System.out.println("=======> " + " Node Name of " + item.getMetadata().getName());
	    }
	    return true;
	}	

	private static boolean listAllDeployments()
	{
		// Call the Client API to get a list of all Deployments
		DeploymentList deploymentList = api.getAllDeployments();
		if(deploymentList.getItems().size() == 0)
		{
			System.out.println("=======> No Deployments available");
			return false;
		}
	    for (Deployment item : deploymentList.getItems()) 
	    {
	    	System.out.println("=======> " + " Deployment Name of " + item.getMetadata().getName() 
	    									+ " in Name Space " + item.getMetadata().getNamespace() 
	    									+ " with a available replicas of " + item.getStatus().getAvailableReplicas());
	    }
	    return true;
	}

	private static boolean listAllServices()
	{
		// Call the Client API to get a list of all Services
		ServiceList serviceList = api.getAllServices(null);
		if(serviceList.getItems().size() == 0)
		{
			System.out.println("=======> No Services available");
			return false;
		}
	    for (Service item : serviceList.getItems()) 
	    {
	    	System.out.println("=======> " + " Service Name of " + item.getMetadata().getName() 
	    										+ " in Name Space " + item.getMetadata().getNamespace()
	    										+ " with Type of " + item.getSpec().getType()
	    										+ " with a Cluster IP of " + item.getSpec().getClusterIP() 
	    										+ " with an External IP of " + ((item.getSpec().getExternalIPs().size() == 0) ? "<none>" : item.getSpec().getExternalIPs().get(0))
	    										+ " with Port Spec of " + item.getSpec().getPorts().get(0).getPort() + (item.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + item.getSpec().getPorts().get(0).getNodePort())  + "/" + item.getSpec().getPorts().get(0).getProtocol());
	    }
	    return true;
	}
	
	private static boolean findServices()
	{
		// Prompt the user for which Service based on Label they want to find
		System.out.print("Enter Namespace to search: ");
		String namespace = reader.next();
		System.out.print("Enter Label Name to search: ");
		String name = reader.next();
		System.out.print("Enter Label Value to search: ");
		String value = reader.next();

		// Call the Client API to search for Service
		HashMap<String, String> labels = new HashMap<String, String>();
		labels.put(name, value);
		ServiceList serviceList = api.findServices(namespace, labels);
		if(serviceList.getItems().size() == 0)
		{
			System.out.println("=======> No Services available");
			return false;
		}
	    for (Service item : serviceList.getItems()) 
	    {
	    	System.out.println("=======> " + " Service Name of " + item.getMetadata().getName() 
											+ " in Name Space " + item.getMetadata().getNamespace() 
    										+ " with Type of " + item.getSpec().getType()
											+ " with a Cluster IP of " + item.getSpec().getClusterIP() 
											+ " with an External IP of " + ((item.getSpec().getExternalIPs().size() == 0) ? "<none>" : item.getSpec().getExternalIPs().get(0))
											+ " with Port Spec of " + item.getSpec().getPorts().get(0).getPort() + (item.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + item.getSpec().getPorts().get(0).getNodePort())  + "/" + item.getSpec().getPorts().get(0).getProtocol());
	    }
	    return true;
	}
	
	private static boolean createNginxService()
	{
		// Set the Service Info and call the Client API to create a Nginx Service
		HashMap<String, String> labels = new HashMap<String, String>();
		labels.put("app", "my-nginx");
		labels.put("environment", "pi");
		labels.put("owner", USER_ID);
		String serviceName = "nginx" + "-" + USER_ID + "-" + System.currentTimeMillis();
		StorageInfo storage = new StorageInfo("nginx" + "-pv", "/usr/share/nginx/html", serviceName + "-html", "small-pvc");
		ServiceInfo info = new ServiceInfo("nginx", serviceName, "default", labels, .5f, 500, 1, 80, 80, storage);
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with Type of " + service.getSpec().getType()
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());
		return true;
	}

	private static boolean createTomcatService()
	{
		// Set the Service Info and call the Client API to create a Tomcat 8.5 Alpine Service
		HashMap<String, String> labels = new HashMap<String, String>();
		labels.put("app", "my-tomcat");
		labels.put("environment", "pi");
		labels.put("owner", USER_ID);		
		String serviceName = "tomcat" + "-" + USER_ID + "-" + System.currentTimeMillis();
		StorageInfo storage = new StorageInfo("tomcat" + "-pv", "/usr/local/tomcat/webapps", serviceName + "-webapps", "small-pvc");
		ServiceInfo info = new ServiceInfo("tomcat:8.5-alpine", serviceName, "default", labels, 1.0f, 1000, 1, 8080, 8080, storage);
		Service service = api.createService(info);
    	System.out.println("=======> " + " Service Name of " + service.getMetadata().getName() 
										+ " in Name Space " + service.getMetadata().getNamespace() 
										+ " with a Cluster IP of " + service.getSpec().getClusterIP() 
										+ " with Type of " + service.getSpec().getType()
										+ " with an External IP of " + ((service.getSpec().getExternalIPs().size() == 0) ? "<none>" : service.getSpec().getExternalIPs().get(0))
										+ " with Port Spec of " + service.getSpec().getPorts().get(0).getPort() + (service.getSpec().getPorts().get(0).getNodePort() == null ? "" : ":" + service.getSpec().getPorts().get(0).getNodePort())  + "/" + service.getSpec().getPorts().get(0).getProtocol());
		return true;
	}

	private static boolean deleteService()
	{
		// Display all Deployments and prompt the user for which Service from Name Space to remove
		if(!listAllDeployments())
			return false;
		System.out.print("Enter Service to remove: ");
		String service = reader.next();
		System.out.print("From Namespace: ");
		String namespace = reader.next();
		
		// Call the Client API to delete the Nginx Service
		boolean ok = api.deleteService(service, namespace);
		System.out.println("=======> Service Deletion Status of " + ok);
		return true;
	}

}
