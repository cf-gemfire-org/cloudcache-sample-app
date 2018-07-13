This sample application connects to GemFire/Geode-based Cloud Cache and creates
a `Region` named `test` as a client region in `PROXY` mode [1].

This means that whatever data the app is going to be writing into that region is
going to be proxied to the server - the GemFire/Geode cluster, which does not
have the `test` region set up yet.

To set up GemFire/Geode cluster with the `test` region, GemFire `gfsh` CLI tool
can be used. Go to https://network.pivotal.io/products/pivotal-gemfire#/releases/4713
to download Pivotal GemFire zip and use `./bin/gfsh` from the unpacked archive
to launch `gfsh`.

> Note: When downloading GemFire from the link above, make sure that the version
that you are downloading is compatible with the version of the GemFire/Geode
cluster that you are using (`9.0.2` in our case matching the libs versions in
`pom.xml`). So that `gfsh` from that package can communicate with the cluster.

After launching `gfsh`, first connect to your GemFire/Geode cluster [2] and
then create the `test` region [3]:
```bash
connect --use-http --url=http://address.for.gfsh.to.access.gemfire \
    --user=username.that.can.create.regions --password=password
#...or as an option if you can access locator IPs directly:
connect --locator=1.2.3.4:10334 \
    --user=username.that.can.create.regions --password=password

create region --name=test --type=REPLICATE
```

> Note: The URL can be looked up in the `VCAP_SERVICES` environment variable
after your application is deployed to CloudFoundry and bound to the Cloud Cache
service instance.

Now this sample application will be able to write to the `test` region which is
going to be proxied to GemFire/Geode cluster.

Tour of `gfsh` https://gemfire.docs.pivotal.io/95/geode/tools_modules/gfsh/tour_of_gfsh.html

Reference:
1. https://gemfire.docs.pivotal.io/95/geode/developing/region_options/region_types.html
2. https://gemfire.docs.pivotal.io/95/geode/tools_modules/gfsh/command-pages/connect.html
3. https://gemfire.docs.pivotal.io/95/geode/tools_modules/gfsh/command-pages/create.html#topic_54B0985FEC5241CA9D26B0CE0A5EA863
