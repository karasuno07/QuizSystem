ARG ELK_VERSION

# https://www.docker.elastic.co/
FROM docker.elastic.co/logstash/logstash:${ELK_VERSION}

# fix bug
RUN sed --in-place "s/gem.add_runtime_dependency \"sinatra\", '~> 2'/gem.add_runtime_dependency \"sinatra\", '~> 2.1.0'/g" /usr/share/logstash/logstash-core/logstash-core.gemspec
RUN /usr/share/logstash/bin/ruby -S /usr/share/logstash/vendor/bundle/jruby/2.5.0/bin/bundle install
# install dependency
RUN /usr/share/logstash/bin/logstash-plugin install logstash-integration-jdbc
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-aggregate
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-mutate
RUN /usr/share/logstash/bin/logstash-plugin install logstash-filter-json

# copy lib database jdbc jars
COPY driver/postgresql-42.3.3.jar /usr/share/logstash/logstash-core/lib/jars/postgresql.jar
