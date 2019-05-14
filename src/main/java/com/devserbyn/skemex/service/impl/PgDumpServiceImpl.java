package com.devserbyn.skemex.service.impl;

import com.devserbyn.skemex.service.DumpService;
import com.devserbyn.skemex.exception.DumpException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("pgDumpService")
@PropertySource("classpath:dataSource.properties")
public class PgDumpServiceImpl implements DumpService {

    private final List<String> staticCommands = new ArrayList<>();
    private final Environment env;

    @Autowired
    public PgDumpServiceImpl(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void postConstruct() throws DumpException {
        String databaseUsername = System.getenv("JDBC_DATABASE_USERNAME");
        String databaseName = env.getProperty("dumpUtil.database");
        String databasePort = env.getProperty("dumpUtil.port");
        String databaseHost = env.getProperty("dumpUtil.host");

        if (isWindows()) {
            staticCommands.add(env.getProperty("dumpUtil.win.bin.path") + "pg_dump.exe");
        } else if (isUnix()) {
            staticCommands.add("pg_dump");
        }else {
            throw new DumpException("This operating system is not supported.");
        }
        staticCommands.add("-h"); //database server host
        staticCommands.add(databaseHost);
        staticCommands.add("-p"); //database server port number
        staticCommands.add(databasePort);
        staticCommands.add("-U"); //connect as specified database user
        staticCommands.add(databaseUsername);
        staticCommands.add("-F"); //output file format (custom, directory, tar, plain text (default))
        staticCommands.add("c");
        staticCommands.add("-b"); //include large objects in dumps
        staticCommands.add("-v"); //verbose mode
        staticCommands.add("-d"); //database name
        staticCommands.add(databaseName);
        staticCommands.add("-f"); //output file or directory name
    }


    @Override
    public void dump() throws DumpException {
        String pathForDump = env.getProperty("dumpUtil.dumps.path");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        String backupFileName = pathForDump + "backup_skemex" + "_" + LocalDateTime.now().format(formatter) + ".sql";

        staticCommands.add(backupFileName);
        try {
            ProcessBuilder pb = new ProcessBuilder(staticCommands);
            pb.environment().put("PGPASSWORD",  System.getenv("JDBC_DATABASE_PASSWORD"));
            Process process = pb.start();

            try (BufferedReader buf = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line = buf.readLine();
                while (line != null) {
                    System.err.println(line);
                    if (line.contains("pg_dump: [custom archiver] could not open output file"))
                        throw new DumpException(line);
                    line = buf.readLine();
                }
            }

            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException ex) {
            throw new DumpException("Something went wrong!");
        }
    }

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static boolean isWindows() {
        return (OS.contains("win"));
    }

    private static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

}

