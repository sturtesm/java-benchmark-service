JMETER_HOME=~ec2-user/apache-jmeter-4.0
TEST=jmeter-performance-stress.jmx
OUTPUT=test-results.jtl

${JMETER_HOME}/bin/jmeter -n -t ${TEST} -l ${OUTPUT}
