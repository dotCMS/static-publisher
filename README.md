
# README
--------

## How to build an OSGi project
-------------------------------

To install all you need to do is build the JAR. to do this run
`./gradlew jar`
This will build a jar in the build/libs directory, refer to the build.gradle for more
information on how this is done.

1. To install this bundle:

Copy the bundle jar file inside the Felix OSGI container (`WEB-INF/felix/load`).
_OR_
Upload the bundle jar file using the dotCMS UI (`CMS Admin->Dynamic Plugins->Upload Plugin`).

2. To uninstall this bundle:

Remove the bundle jar file from the Felix OSGI container (`WEB-INF/felix/load`).
_OR_
Undeploy the bundle using the dotCMS UI (`CMS Admin->Dynamic Plugins->Undeploy`).

## Notes
--------
In order to create this OSGI plugin, you must create a META-INF/MANIFEST to be inserted into OSGI jar.
This file is being created for you by Gradle. If you need you can alter our config for this but in general our out of the box config should work.
The Gradle plugin uses BND to generate the Manifest. The main reason you need to alter the config is when you need to exclude a package you are including on your Bundle-ClassPath

If you are building the MANIFEST on your own or desire more info on it below is a description of what is required
in this MANIFEST you must specify (see template plugin):

Bundle-Name: The name of your bundle

Bundle-SymbolicName: A short an unique name for the bundle

Bundle-Activator: Package and name of your Activator class (example: com.dotmarketing.osgi.viewtools.Activator)

DynamicImport-Package: *
    Dynamically add required imports the plugin may need without add them explicitly

Import-Package: This is a comma separated list of package's name.
                In this list there must be the packages that you are using inside
                the bundle plugin and that are exported by the dotCMS runtime.
