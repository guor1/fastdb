package fastdb.core;

import org.fastdb.DB;

public class H2Database {

    public static void initTable() {
        DB.createNativeQuery("DROP TABLE IF EXISTS `wdyq_contex`").executeUpdate();
        DB.createNativeQuery(
                "CREATE TABLE `wdyq_contex` (`id` int(10) NOT NULL AUTO_INCREMENT, `host` varchar(100) NOT NULL, `doctype` varchar(10) NULL, `template` varchar(255) NOT NULL, `expression` varchar(255) NOT NULL,`user` int(10) NOT NULL, `lastoptime` timestamp NULL, PRIMARY KEY (`id`), UNIQUE KEY `template_uk` (`template`) USING BTREE) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8")
                .executeUpdate();

        DB.createNativeQuery("DROP TABLE IF EXISTS `wdyq_user`").executeUpdate();
        DB.createNativeQuery(
                "CREATE TABLE `wdyq_user` (`id` int(10) NOT NULL AUTO_INCREMENT, `username` varchar(100) NOT NULL, PRIMARY KEY (`id`), UNIQUE KEY `username_uk` (`username`) USING BTREE) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8")
                .executeUpdate();
    }
}
