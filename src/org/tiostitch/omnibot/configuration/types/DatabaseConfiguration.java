package org.tiostitch.omnibot.configuration.types;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public final class DatabaseConfiguration {

    @Getter @Setter @NoArgsConstructor
    public static final class MYSQLConfiguration {

        @SerializedName("HOST")
        private String MYSQL_HOST;

        @SerializedName("DATABASE")
        private String MYSQL_DATABASE;

        @SerializedName("USERNAME")
        private String MYSQL_USERNAME;

        @SerializedName("PASSWORD")
        private String MYSQL_PASSWORD;

    }

    @Getter @Setter @NoArgsConstructor
    public static final class SQLITEConfiguration {

        @SerializedName("FILE_NAME")
        private String FILE_NAME;

    }

    @SerializedName("DATA_TYPE")
    private String DATA_TYPE;

    @SerializedName("MYSQL_CONFIG")
    private MYSQLConfiguration MYSQL_CONFIG;

    @SerializedName("SQLITE_CONFIG")
    private SQLITEConfiguration SQLITE_CONFIG;

}

