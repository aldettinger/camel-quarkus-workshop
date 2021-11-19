# Camel Quarkus Workshop

## Prerequisites
 * JDK 11 installed
 * Maven >= 3.8.1 installed with JAVA_HOME configured appropriately
 * Docker >= 1.13.1 installed

 Let's check that those prerequisites are installed, for instance like below:

```
[dev@camel-quarkus-workshop]$ mvn --version
Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: /home/agallice/dev/maven/apache-maven-3.8.1
Java version: 11.0.11, vendor: AdoptOpenJDK, runtime: /home/dev/.sdkman/candidates/java/11.0.11.hs-adpt
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "3.10.0-1160.45.1.el7.x86_64", arch: "amd64", family: "unix"
[dev@camel-quarkus-workshop]$ java --version
openjdk 11.0.11 2021-04-20
OpenJDK Runtime Environment AdoptOpenJDK-11.0.11+9 (build 11.0.11+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK-11.0.11+9 (build 11.0.11+9, mixed mode)
[dev@camel-quarkus-workshop]$ docker --version
Docker version 1.13.1, build 7f2769b/1.13.1
```

Describing all the ways to have those prerequisites installed is beyond the scope of this workshop, still some useful links could be found below:
 * [https://maven.apache.org/install.html](https://maven.apache.org/install.html)
 * [https://sdkman.io/usage](https://sdkman.io/usage)
 * [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/)
 * [https://medium.com/@gayanper/sdkman-on-windows-661976238042](https://medium.com/@gayanper/sdkman-on-windows-661976238042)
 * [https://maven.apache.org/guides/getting-started/windows-prerequisites.html](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)

As a last step, let's clone the workshop github project locally in a folder of your choice, let's call this folder  ${CQ_WORKSHOP_DIRECTORY} :

```
cd ${CQ_WORKSHOP_DIRECTORY}
git clone git@github.com:aldettinger/camel-quarkus-workshop.git 
```

Note that during the workshop, you'll have to replace ${CQ_WORKSHOP_DIRECTORY} by the folder you have choosen.
For instance, one can list the folder present in the workshop folder as below:

```
ls "${CQ_WORKSHOP_DIRECTORY}/camel-quarkus-workshop"
```

You should see something similar to below:

```
part-2-jvm-mode
part-3-native-mode
part-4-extensions
part-5-routes
part-6-eips
```

There is currently no folder starting with "part-1-" ! Well spotted, that's totally fine as we'll create it in the next section.
Setting up the requirements should be done now: Congratulations !

## Quarkus DEV mode (20 minutes)

In this section, we'll have our first contact with Quarkus. Indeed, Quarkus offers multiple modes DEV, JVM, native... for multiple purposes.
The idea behind the DEV mode is to simplify developers life. Better than words, let's start the creation of a Camel Quarkus project.

As such, let's create a first terminal, we'll call it the *DEV terminal*.
In the *DEV terminal*, type commands as below:

```
cd "${CQ_WORKSHOP_DIRECTORY}/camel-quarkus-workshop"
mvn io.quarkus:quarkus-maven-plugin:2.4.2.Final:create
```

We need to specify the artifactId, extensions and code start. For groupId and version, simple press enter as it was done below:

```
Set the project groupId: 
Set the project artifactId: part-1-dev-mode
Set the project version: 
What extensions do you wish to add (comma separated list): platform-http
Would you like some code to start (yes), or just an empty Quarkus project (no): no
```

Let's start the project, for instance:

```
cd part-1-dev-mode
mvn quarkus:dev
```

Quarkus has just started in DEV mode and print interesting logs like the Camel version, the Quarkus version, start time and so on:

```
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2021-11-19 10:04:46,708 INFO  [org.apa.cam.qua.cor.CamelBootstrapRecorder] (Quarkus Main Thread) Bootstrap runtime: org.apache.camel.quarkus.main.CamelMainRuntime

2021-11-19 10:04:46,747 INFO  [org.apa.cam.imp.eng.AbstractCamelContext] (Quarkus Main Thread) Routes startup summary (total:0 started:0)
2021-11-19 10:04:46,747 INFO  [org.apa.cam.imp.eng.AbstractCamelContext] (Quarkus Main Thread) Apache Camel 3.12.0 (camel-1) started in 11ms (build:0ms init:9ms start:2ms)
2021-11-19 10:04:46,841 INFO  [io.quarkus] (Quarkus Main Thread) part-1-dev-mode 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.4.2.Final) started in 1.955s. Listening on: http://localhost:8080
2021-11-19 10:04:46,842 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2021-11-19 10:04:46,843 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [camel-attachments, camel-core, camel-platform-http, cdi, smallrye-context-propagation, vertx]

```

Now, let's create a second terminal, we'll call it the *USER terminal*.

The next step is to configure our first camel route by creating the file `src/main/java/org/acme/MyRoutes.java`.
For instance, it can be done from the *USER terminal*, by typing the following commands:

```
cd ${CQ_WORKSHOP_DIRECTORY}/part-1-dev-mode
mkdir -p src/main/java/org/acme/
touch src/main/java/org/acme/MyRoutes.java
```

Hey, wait something has just happened in the quarkus *DEV terminal*.
Indeed, Quarkus has detected a change in the application source code and automatically reloaded the project.

Now let's implement our first camel route in `src/main/java/org/acme/MyRoutes.java` as below:

```
package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoutes extends RouteBuilder {

    @Override
    public void configure() {
        from("platform-http:/cq-http-endpoint").setBody(constant("Hello Camel Quarkus in DEV mode !"));
    }
}
```

We'll explain a bit more the camel syntax later on.
But at this stage, it should be enough to know that we have just created a Camel application listening for incoming HTTP requests and answering with an hard-coded response.

Let's test that with your favorite HTTP client. For instance, in the *USER terminal*, type the following command:

```
curl http://localhost:8080/cq-http-endpoint
```

We should see the HTTP response as below:

```
Hello Camel Quarkus in DEV mode !
```

Now let's update the hard-coded response to `Hello Camel Quarkus from the 3h workshop room !`:

```
from("platform-http:/cq-http-endpoint").setBody(constant("Hello Camel Quarkus from the 3h workshop room !"));
```

Let's test again with your favorite HTTP client. For instance, in the *USER terminal*, type the following command:

```
curl http://localhost:8080/cq-http-endpoint
```

We see that the message has been updated:

```
Hello Camel Quarkus from the 3h workshop room !
```

Now, let's stop the quarkus DEV mode by pressing `q` or `CTRL+C` in the *DEV terminal*.
We should see logs stating that the Camel routes are stopping as below:

```
2021-11-19 13:06:22,338 INFO  [org.apa.cam.imp.eng.AbstractCamelContext] (Quarkus Main Thread) Routes shutdown summary (total:1 stopped:1)
2021-11-19 13:06:22,338 INFO  [org.apa.cam.imp.eng.AbstractCamelContext] (Quarkus Main Thread)     Stopped route1 (platform-http:///cq-http-endpoint)
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  6.668 s
[INFO] Finished at: 2021-11-19T13:06:22+01:00
[INFO] ------------------------------------------------------------------------
```

That's it for our first contact with the Quarkus DEV mode.
We have seen that it offer a continuous feedback loop to help with development.
The DEV mode offers few more features. When you have time, we encourage you to take a look at:
 * [https://quarkus.io/guides/continuous-testing](https://quarkus.io/guides/continuous-testing)
 * [https://quarkus.io/guides/dev-services](https://quarkus.io/guides/dev-services)

@TODO: Only 20 minutes ? Do we add continuous testing ?

## Quarkus JVM mode (@TODO minutes)

With the JVM mode, we enter into the core of the Quarkus philosophy.
The bottom line being that the historical trade-offs used in the JVMs should change as we are now coding the cloud era.
For instance, in a world of containers, JVM are started and stopped more frequently.
In this respect, it makes sense to do as much as possible during at build time.

@TODO: Let's integrate here the schema showing what Quarkus does at build time vs typical Java framework.

Building a Camel Quarkus route in JVM mode is simple. In the *DEV terminal*, type commands as below:

```
cd ${CQ_WORKSHOP_DIRECTORY}/part-2-jvm-mode
mvn clean package
```

It should take only few seconds.
Let's look at the produced artifacts as it was done below:

```
ls target/quarkus-app
```

It should exhibit a directory structure as below:

```
app  lib  quarkus  quarkus-app-dependencies.txt  quarkus-run.jar

```

Everything needed is here.
In JVM mode, the application has actually been packaged as a **fast-jar**.
Indeed, Quarkus has even designed its own packaging format in order to provide faster startup times. @TODO: a link explaining fast-jar woudl be good.
so far so good, we can start our Camel route in JVM mode as shown below:

```
java -jar target/quarkus-app/quarkus-run.jar
```

At this stage, we'll record the startup times. Please locate the two lines that looks like below:

```
2021-11-19 13:39:23,254 INFO  [org.apa.cam.imp.eng.AbstractCamelContext] (main) Apache Camel 3.12.0 (camel-1) started in 42ms (build:0ms init:35ms start:7ms)
2021-11-19 13:39:23,332 INFO  [io.quarkus] (main) part-2-jvm-mode 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.4.2.Final) started in 0.825s. Listening on: http://0.0.0.0:8080
```

Pay attention to the camel start times and also to the Quarkus start time.
You can compare them with your neighbors if you'd like :) But the important part is to remind them for the next section.

Now let's  check that our Camel Quarkus route in JVM mode behaves the same way as in DEV mode.
In the *USER terminal*, use you favorite HTTP client, for instance:

```
curl localhost:8080/cq-http-endpoint

```

We should have the same answer as in part 1:

```
Hello Camel Quarkus from the 3h workshop room !
```

At this point, we have seen that Quarkus can start quicker than typical Java frameworks in JVM mode.
In a container world where the time to serve the first request is a key metric, it's a huge advantage.
Quarkus offers other significant improvements, for instance related to startup memory.
When you have time, we encourage you to have a look at the RSS memory used on startup.
Unix users could find the command `ps -e -o rss,comm,args | grep "quarkus-run.jar$"` useful.

Well done for the JVM mode ! Let's tackle a more tricky part now. In next section, we'll try to compile our Camel Quarkus route as a native executable.

## Quarkus native mode (@TODO minutes)

The Quarkus philosophy is to move as much tasks as possible at build time.
In this respect, the native mode is going one step further in this direction.
The native mode is based on a different kind of virtual machine, namely the [SubstrateVM](https://docs.oracle.com/en/graalvm/enterprise/20/docs/reference-manual/native-image/SubstrateVM/#:~:text=Substrate%20VM%20is%20an%20internal,JVM%20Compiler%20Interface%20(JVMCI).) from the [GraalVM project](https://www.graalvm.org/).

In native mode, a lot more happen ahead of time. For instance, most Java static initializers could be expected to be executed once and for all at build time.
Indeed, most Java static initializers are performing some tasks that are not runtime dependent.
So, why should we wait the last minute to perform those tasks.

Completing the setup to enable native compilation could be a bit tricky during a workshop.
So, we'll have a try with [Creating a Linux executable without GraalVM installed](https://quarkus.io/guides/building-native-image#container-runtime).

In the *DEV terminal*, let's trigger a native build by activating the `native` profile:

```
cd ${CQ_WORKSHOP_DIRECTORY}/part-3-native-mode
mvn clean package -Pnative -Dquarkus.native.container-build=true
```

That's taking time. Docker may trigger the download of few images but this should be done only once.
Once the download has completed, we still have more time to wait as the native build is triggered and produces logs like below:

```
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]    classlist:   4,543.25 ms,  1.19 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]        (cap):     703.18 ms,  1.19 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]        setup:   3,220.96 ms,  1.19 GB
15:57:42,350 INFO  [org.jbo.threads] JBoss Threads version 3.4.2.Final
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]     (clinit):     869.01 ms,  4.96 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]   (typeflow):  36,279.52 ms,  4.96 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]    (objects):  33,069.17 ms,  4.96 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]   (features):   1,246.40 ms,  4.96 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]     analysis:  73,793.24 ms,  4.96 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]     universe:   3,843.29 ms,  4.96 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]      (parse):   3,443.30 ms,  4.96 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]     (inline):   5,169.39 ms,  5.11 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]    (compile):  56,585.98 ms,  5.03 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]      compile:  68,176.68 ms,  5.03 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]        image:   7,373.91 ms,  5.51 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]        write:   1,232.55 ms,  4.43 GB
[part-2-jvm-mode-1.0.0-SNAPSHOT-runner:26]      [total]: 163,172.47 ms,  4.43 GB
```

It looks that there are really a lot of things happening at build time and it's taking long.
This is one of the downside of the native mode: slow build, hard debugging, no just-in-compilation of Java code and few developments trick could be needed when using Java dynamic features like reflection.

Let's see what we have produced, for instance by typing the command below

```
ls -al target/*runner
```

It should show a native executable a bit like this one:

```
-rwxr-xr-x. 1 devuser devuser 60137560 Nov 19 18:59 target/part-3-native-mode-1.0.0-SNAPSHOT-runner
```

The size of 57 MiB may seems big for a Java application but note that no JDK is needed to run this.
Indeed, during the long native compilation phase, all necessary parts from the JDK and third party libraries have been selected and added to the native executable.

Now let's start our Camel Quarkus native route with the following command:

```
target/*runner
```

Like we did in JVM mode, we'll record the startup times. Please locate the two lines that looks like below:

```
2021-11-19 17:08:31,169 INFO  [org.apa.cam.imp.eng.AbstractCamelContext] (main) Apache Camel 3.12.0 (camel-1) started in 1ms (build:0ms init:1ms start:0ms)
2021-11-19 17:08:31,173 INFO  [io.quarkus] (main) part-3-native-mode 1.0.0-SNAPSHOT native (powered by Quarkus 2.4.2.Final) started in 0.021s. Listening on: http://0.0.0.0:8080
```

Please pay attention at the Camel init/start time and also the Quarkus start time.
Now you may think that the long native compilation may be worth the challenge in some situation where quick container starting is required.

Now let's  check that our Camel Quarkus route in native mode behaves the same way as in DEV and JVM mode.
In the *USER terminal*, use you favorite HTTP client, for instance:

```
curl localhost:8080/cq-http-endpoint

```

We should have the same answer as in part 1 and 2:

```
Hello Camel Quarkus from the 3h workshop room !
```

Reaching this point, we have scratched the surface of the JVM and native mode and should retain few lessons.
The application behaves the same in JVM and native mode but the performance profile is not the same.
The native mode and JVM mode are both great but in distinct scenarios.

There are still a lot of things to know about the native mode.
When you have time, we encourage you to have a look at the RSS memory used on startup.
Unix users could find the command `ps -e -o rss,comm,args | grep "quarkus-run.jar$"` useful.

A big congrats for having learn the native mode ! It was a tricky part and maybe some were not able to build the native executable.
From now on, we'll only use the DEV mode as it will be sufficient to show the Camel concepts.

## Camel Quarkus Routes (@TODO minutes)

Camel allow to consume messages from 300+ technologies and to produce them to 300+ technologies.
Let's have a base project with some holes to be completed ?
+ like 'extends RouteBuilder'
+ another hole in XML, or in application.properties to load the XML ?

The route logic could be very simple. from(timer).setBody(...).log.

## Camel Quarkus Extensions (@TODO minutes)

Explain camel components and how they are derived to become Camel Quarkus extensions ?
Then, show how a very simple integration logic (dumb pipe... from A to B).
There could be an excercice to transfer message from 2/3 technos to 2/3 other target techno.
And the trick would be that the very simple logic could be applied.
At the end of the day, participants only change extensions. We could have DEV services, or maybe Test resources ?

## Camel Quarkus Enterprise Integration Patterns (@TODO minutes)

Then we add some eips => from/eips/to ?

## TODO: more sections needed ? camel-bean ? some cdi tricks ?

## Satisfation form ? Reward/Goodies ?
