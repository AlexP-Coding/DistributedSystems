This is a prototype of the demo directory.
It will be finalized and delivered by Project Delivery Phase #3.


# To run server (debug mode off)
```s
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