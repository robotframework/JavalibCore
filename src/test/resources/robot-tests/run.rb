#!/usr/bin/ruby

require 'fileutils'

def find_root(file)
  if File.exist?("#{Dir.pwd}/#{file}")
    "#{Dir.pwd}/#{file}"
  else
    find_root("../#{file}")
  end
end

def root_dir
  this_dir = Dir.pwd
  Dir.chdir(File.dirname(find_root('pom.xml')))
  retval = yield(Dir.pwd)
  Dir.chdir(this_dir)
  retval
end

ENV['CLASSPATH'] = root_dir do |dir|
  unless File.directory?("#{dir}/target/test-classes")
    system "mvn test-compile"
  end

  unless File.directory?("#{dir}/target/dependency")
    system "mvn dependency:copy-dependencies -Dscope=test"
  end

  classpath = Dir["#{dir}/target/dependency/*.jar"] << "#{dir}/target/classes" << "#{dir}/target/test-classes"
  classpath.join(':')
end

system "jybot --loglevel TRACE --outputdir /tmp #{ARGV.join(' ')}"
