/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

import org.codehaus.groovy.tools.FileSystemCompiler as Compiler


def benchData = [
    hello           :   [1],
    ackermann       :   [5, 6, 7, 8],
    ary             :   [10,100,1000,1000000],
/*    binarytrees     :   [1],
    chameneos       :   [1],
    echo            :   [1],
    except          :   [1],
    fannkuch        :   [1],
    fannkuchredux   :   [1],
    fasta           :   [1],*/
    fibo            :   [30, 31, 32, 33, 34],
/*    harmonic        :   [1],
    hash            :   [1],
    hash2           :   [1],
    heapsort        :   [1],
    knucleotide     :   [1],
    lists           :   [1],
    magicsquares    :   [1],
    mandelbrot      :   [1],
    matrix          :   [1],
    message         :   [1],
    meteor          :   [1],
    methcall        :   [1],
    moments         :   [1],
    nbody           :   [1],
    nestedloop      :   [1],
    nsieve          :   [1],
    nsievebits      :   [1],
    objinst         :   [1],
    partialsums     :   [1],
    pidigits        :   [1],
    process         :   [1],
    prodcons        :   [1],
    random          :   [1],
    raytracer       :   [1],
    recursive       :   [1],
    regexdna        :   [1],
    revcomp         :   [1],
    sieve           :   [1],
    spectralnorm    :   [1],
    spellcheck      :   [1],
    strcat          :   [1],
    sumcol          :   [1],
    takfp           :   [1],
    threadring      :   [1],
    wc              :   [1],
    wordfreq        :   [1],*/
]

// Find base directory for script to allow executing from other directories
benchmarkDir = new File(getClass().protectionDomain.codeSource.location.path).parent
execDir = new File(benchmarkDir, 'exec')
if (execDir.exists()) {
    cleanFolder()
} else {
    execDir.mkdir()
}

groovyAllJar = getGroovyAllJar()

horizontalBreak()
println "Groovy benchmarking test"
showJavaVersion()
println "Groovy lib: $groovyAllJar"
horizontalBreak()
def executeBench= { bench, input ->
    println "Benchmark $bench"
    [".java", ".groovy"].each { ending ->
        println("\t$bench$ending :")
        
        boolean exists = prepare(bench, ending)
        if (exists) {
            execBenchmark(bench, input)
            if (ending == '.groovy') {
                cleanFolder()
                println "\t$bench$ending (indy) : "
                prepare(bench, ending, true)
                execBenchmark(bench, input)
            }
        } else {
            println "skipped"
        }
        cleanFolder()
    }
    horizontalBreak()
}
if (args.length==0) { 
    benchData.each(executeBench)
} else {
    executeBench(args[0],benchData[args[0]])
}
execDir.delete()

void horizontalBreak() {
    println "-" * 80
}

boolean prepare(bench, ending, indy=false) {
    // copy file to exec folder it it exists
    def orig = new File(benchmarkDir, "bench/$bench$ending")
    if (!orig.exists()) return false;

    // compile file using groovy compiler
    // in = orig, out = exec
    def args = ['-j', '-d', execDir.absolutePath]
    if (indy) {
        args << '-indy'
    }
    args << orig.absolutePath
    Compiler.main(args as String[])
    true
}

void cleanFolder(){
    execDir.eachFile { file -> file.delete() }
}

String javaHome() {
    def path = System.getenv("JAVA_HOME")
    if (!path.endsWith("$File.separatorChar")) path += File.separatorChar
    return path
}

String javaCommand() {
    javaHome() + "bin/java"
}

void showJavaVersion() {
    def p = [javaCommand(), "-version"].execute()
    p.consumeProcessErrorStream(System.out)
    p.consumeProcessOutputStream(System.out)
    p.waitForOrKill(10 * 60 * 1000)
}

void execBenchmark(bench, input) {
    input.each { param -> 
        def cp = execDir.absolutePath + File.pathSeparatorChar
        cp += groovyAllJar + File.pathSeparatorChar

        def time1 = System.nanoTime()

        print "\t\trunning  "
        20.times { n->
            printProgress(n)
            def p = [javaCommand(), "-cp", cp, bench, param].execute()
            p.consumeProcessOutputStream(null)
            p.consumeProcessErrorStream(System.err)
            p.waitForOrKill(10 * 60  *1000)
        }
        def time2 = System.nanoTime()
        long td = (time2-time1) / 1000000 / 20
        print "\b" * 9
        println "time ($param) = $td"
    }
}

void printProgress(it) {
  def n = it % 4
  if (n==0) print "\b/"
  if (n==1) print "\b-"
  if (n==2) print "\b\\"
  if (n==3) print "\b-"
}

String getGroovyAllJar() {
    String jar = ''
    new File(System.getProperty('groovy.home'), 'embeddable').eachFile { entry ->
        if (entry.name.startsWith('groovy-all')) {
            jar = entry.absolutePath
        }
    }
    return jar
}