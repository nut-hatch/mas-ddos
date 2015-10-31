# mas-ddos
Multi Agent System based on Jade performing a DDOS attack (opening a Socket to a specified server).

Starting a Coordinator Agent:
java -cp JADE_PATH:./bin jade.Boot -gui -jade_domain_df_maxresult 100000 Coordinator:lab4.CoordinatorAgent

Starting a SubCoordinator Agent:
java -cp JADE_PATH:./bin:./ jade.Boot -container -container-name LOCAL -local-port 1098 SubCoordinator:lab4.SubCoordinatorAgent

See run.sh
