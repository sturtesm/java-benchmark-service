JMETER_HOME=~ec2-user/apache-jmeter-4.0

${JMETER_HOME}/bin/jmeter -n -t jmeter-performance-benchmark.jmx -l test-results.jtl
