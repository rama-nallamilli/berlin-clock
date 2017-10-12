
# Berlin Clock in Scala

The berlin clock is a clock which tells the time using a series of illuminated coloured lights.  More details can be found here: https://en.wikipedia.org/wiki/Mengenlehreuhr

![alt text](https://upload.wikimedia.org/wikipedia/commons/thumb/4/4f/Berlin-Uhr-1650-1705.gif/170px-Berlin-Uhr-1650-1705.gif "Berlin Clock in Action")

# Usage

You can run the Berlin clock using sbt:
```
sbt "run-main org.rntech.runner.Main"
```

The number of iterations and sleep between clocks can be overriden by passing parameters respectively:
```
sbt "run-main org.rntech.runner.Main 10 2000"
```

Running the tests:
```
sbt clean test
```