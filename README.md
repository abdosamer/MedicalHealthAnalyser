#                                                         Medical Health analyser
#                                                                 (MHA)

## Abstract:
Hospital information systems are covering clinical, administrative and managerial applications. Today care providers and administrators can access libraries of information resources and longitudinal patient records (with the corresponding permissions). Computer networks in the hospital reduce costs and improve the quality of care. These systems will be built of modular components that are integrated across multiple settings in which patient receive necessary care. Thus, it is the time to encourage physicians for utilizing state of the art information technology to conduct and manage all the aspects of the medical process. The PECTMS can be installed in hospitals as well as regional centers and private offices, with applications in almost every medical field. PECTMS is an application generator for medical information systems, where its basic concept is a clear, comprehensive, easily accessible and readily retrievable patient record.

## Introduction:
The majority of medical information systems in use today have not been able to penetrate the medical production floor, where the actual medical process takes place and where the bulk of medical information is created. Namely, the patient-physician session, which is the basic unit of the medical process, does not involve online, systematic use of computerized information systems. Computerizing the production floor is not an end in itself. Rather, its importance lies in being part of the organization's ability to implement online and long range planning and control tools.

Based on the concept of a comprehensive patient record and using advanced human interface techniques, the electronics and communications department has managed to make that breakthrough. The objective is to create a standard tool to conduct, manage and control the entire medical situation, online during the patient-physician session.

The work of medical staff is judged on the quality of medical care rather than on whether or not they are using management enforced computer systems or how well they use them. Therefore, any computer system must cross a motivational threshold before doctors and other medical staff would incorporate it into their daily work. Conventional systems, whether written in code as dedicated systems or developed as applications under conventional application generators, have not been able to pass that threshold.

It is aimed to build an application generator designed specifically for the medical environment, with complete emulation of medical work patterns. The medical environment is characterized by the different kinds of data it must handle concurrently. Also typical are frequent, often rapid changes arising from the very nature of the medical profession. That is, it should handle free text, formatted text, numeric data, pictures and graphs all within the same record. It employs advanced human interface techniques to enter, display, access, manage and retrieve this information, including transformation of graphic information into text and vice versa.

The flexibility of the database structure should allow frequent changes with the utmost degree of tolerance. For example, new data screens (templates) can be added, others modified or even removed. Or, new fields can be added to the specific application, other modified, moved around in location, enlarged or reduced in size and so on. These changes can be made conveniently any time, without disrupting existing data or interfering with the physician's daily work. The success is to develop high quality solutions offered to physicians in creating and managing their clinical information. Thus, the approach should enable to compile the database of the medical information system exactly where it is created, incorporating all the elements that make up the medical process into one database.

The system contains embedded mechanisms for mutual feedback channels, while they can be used to give online feedback to physicians to enrich medical processes as described above. These channels also enable organization and management levels to incorporate control and medical auditing measures into the basic unit of the work environment. For example, supplementary medical information and the most recent treatment procedures, as well as new regulatory requirements and administrative regulations can be communicated directly, online and within the proper context of the medical process.
By using these channels, management and organization levels can gain insight into the very nature of the medical process, which the place and time where their money is spent. Perhaps for the first time, the interrelations between the various elements that make up the medical process can be made apparent. Thus, the system enables management levels to influence the quality of the medical process as well as enhance the administrative decision-making. This can be done online, at the proper time and location.

 

Ideally, health care is a communication-intensive process and information that relates to problem-solving and treatment regimens is exchanged constantly among health workers and between patients and their health-care providers. The primary health-care teams in a remote area should be linked with their base hospital, regional hospitals, a teaching hospital and an aerial retrieval team. This project describes the changes in the health workers' consultations and in their patient-management practices when they used the satellite communications network. These changes resulted in improved health care and reduced some health-care costs.















Greater attention has been given recently to information technology and telecommunication reforms and their use for the improvement of health care service delivery. Telemedicine is the use of advanced telecommunication technologies for the purposes of making diagnoses, conducting research, transferring patient data, improving disease management and treatment in remote areas. This project is devoted to the design and implementation of telemedicine system and provides a brief overview of telemedicine, its potential clinical applications, and the various benefits and leading issues surrounding it.

The design process includes the following points: 
```
1-	Realize a medical monitoring system for patient vital signs.
2-	Establish connection between database and all system modules.
3-	Design a website to serve as a link between doctor, hospital, CarePro device [5], CarePro Android App[6], and patient.
4-	Use a GPS module in conjunction with INS to track Patient's location for fast medical response in an emergency situation.
5-	Design (CarePro) an eHealth care device  for measuring the difference in biometric parameters by various medical sensors that are attached to it 
6- Design (CarePro Android App) an android app that can  be attached to the CarePro device via Bluetooth to show the measured data by the device in graphs or written data and see the patient profile by connecting to the website 

```
### Advantages and Characteristics:
The system features have an immediate effect on the quality of medical care and establish a readily accessible database for further research and analysis;
-	Utilizes advanced data entry techniques,
-	Gives physicians immediate access to clear, detailed patient records including high quality presentation of graphic information from video and other sources
-	Provides online access to relevant background information (e.g. sensitivities) enables immediate retrieval of cross referenced information for preventive medicine,
-	It can give immediate feedback on suggested procedures and so on.
-	Legal and ethical aspects have been given special attention and data access restrictions can be enforced at all system levels, starting from individual fields that most of the data will be encrypted and only the supervisor Doctor and the patient only who can access it
-	keeping patient up to data about his health status via the android app 

#### The system is characterized by;
-	Text oriented in design, which makes it highly suitable for handling medical information and for supporting the decision-making processes of the medical profession.
-	Variability: Applications for any medical field can be designed with great variability in record design, to suit the needs and requirements of the institution or the medical field and the personal preferences of the end users.
-	Flexibility: Changes can be made any time to adapt the system to new medical, regulatory or organizational circumstances, without disrupting existing data.
-	Connectivity and Communication: Connectivity to other systems is an integral part of PECTMS (Server and others), through embedded import/export mechanisms, which is being customized for every organization. This makes it possible for each level of the organization to obtain online those fractions of the database relevant to performing its tasks.
-	Design Techniques: Interchangeable top-down and other design techniques are available, enabling to design different parts of the system concurrently or at different stages, then incorporate the different elements into one coherent system.

### Sub-Branches/Desciplines:
The following Sub-Branches/Desciplines are to be tackled for achieving this project;
-	Wireless communications; radio transceivers.
-	Embedded Systems: Sensors Master System, navigation, localization, GPS, INS
-	Biomedical sensors; types, conditioning networks (software and hardware)
-	WSN: wireless sensor networks structure and implementation.
-	IoT: Internet of things structure and implementation.
-	DataBase architecture design and implementation.
-	Medicine: telemedicine, information technology (symptoms, diagnosis, treatment)
-	Pharmacy: Tele-pharmacy
-	Website design, android app design, and network implementation

## Devices,apps archeticture and Design

### CarePro Embedded Device

![alt text](http://www.my-signals.com/wp-content/themes/mysignals/images/mysignals-with-sensors-connected.jpg)

##### it will be Desing in the apstraction layers Design pattern that will help it setup be easy with the web app and mobile app to send the data for them via wifi and bluetooth module that the archetecture will be :


- App Layer
- SoftWare Apstraction layer SAL 
- hardware abstraction layer HAL
  - Sensors Drivers
  - WIFI Module Driver
  - Bluetooth Module Driver
  
  
### MHA Android App

![alt text](https://www.cooking-hacks.com/media/cooking/images/documentation/mysignals_software/mysignals_box_smartphone_small.jpg)

##### it  will be designed by the MVVM (model view - view model) Design pattern that it will recieve the data from the CarePro Embbedded Device via Bluetooth to show the data of the sensors while measuring in real time and connect to the website in the same time to get patient previous data 

  


