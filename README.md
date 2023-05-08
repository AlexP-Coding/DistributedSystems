# DistributedSystems
Distributed systems project for the class of 2021/2022 using Object-Oriented Programming with protobuf and gRPC

## Authors

**Group A06**

### Team Members


| Number | Name           | User                              | Email                                      |
|--------|----------------|-----------------------------------|--------------------------------------------|
| 97124  | Jiaqi Yu       | <https://github.com/jiaqiyusun>   | <mailto:jiaqi.yu@tecnico.ulisboa.pt>       |
| 92420  | André Matos    | <https://github.com/andrem000>    | <mailto:andre.matos@tecnico.ulisboa.pt>    |
| 97375  | Alexandra Pato | <https://github.com/AlexP-Coding> | <mailto:alexandra.pato@tecnico.ulisboa.pt> |

## Getting Started

The overall system is made up of several modules. The main server is the _ClassServer_. The clients are the _Student_,
the _Professor_ and the _Admin_. The definition of messages and services is in the _Contract_. The future naming server
is the _NamingServer_.

See the [Project Statement](https://github.com/tecnico-distsys/Turmas) or a complete domain and system description.

### Prerequisites

The Project is configured with Java 17 (which is only compatible with Maven >= 3.8), but if you want to use Java 11 you
can too, just downgrade the version in the POMs.

To confirm that you have them installed and which versions they are, run in the terminal:

```s
javac -version
mvn -version
```

### Installation

To compile and install all modules:

```s
mvn clean install
```

## Built With

* [Maven](https://maven.apache.org/) - Build and dependency management tool;
* [gRPC](https://grpc.io/) - RPC framework.


# To run server (debug mode off)
```
mvn compile exec:java -Dexec.args=""
```

# To run server (debug mode on)
```s
mvn compile exec:java -Dexec.args="-debug"
```

# To run Student
```s
mvn compile exec:java -Dexec.args="alunoXXXX StudentName"
```

# To run Professor
```s
mvn compile exec:java -Dexec.args=""
```

# To run Admin
```s
mvn compile exec:java -Dexec.args=""
```