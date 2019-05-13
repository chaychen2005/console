package console.precompiled;

import com.fasterxml.jackson.databind.ObjectMapper;
import console.common.CRUDParseUtils;
import console.common.Common;
import console.common.ConsoleUtils;
import console.common.HelpInfo;
import console.common.TableInfo;
import console.exception.ConsoleMessageException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.jsqlparser.JSQLParserException;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.precompile.common.PrecompiledCommon;
import org.fisco.bcos.web3j.precompile.config.SystemConfigSerivce;
import org.fisco.bcos.web3j.precompile.consensus.ConsensusService;
import org.fisco.bcos.web3j.precompile.crud.CRUDSerivce;
import org.fisco.bcos.web3j.precompile.crud.Condition;
import org.fisco.bcos.web3j.precompile.crud.Entry;
import org.fisco.bcos.web3j.precompile.crud.EnumOP;
import org.fisco.bcos.web3j.precompile.crud.Table;
import org.fisco.bcos.web3j.protocol.ObjectMapperFactory;
import org.fisco.bcos.web3j.protocol.Web3j;

public class PrecompiledImpl implements PrecompiledFace {

    private Web3j web3j;
    private Credentials credentials;

    @Override
    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }

    @Override
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public void addSealer(String[] params) throws Exception {
        if (params.length < 2) {
            HelpInfo.promptHelp("addSealer");
            return;
        }
        if (params.length > 2) {
            HelpInfo.promptHelp("addSealer");
            return;
        }
        String nodeId = params[1];
        if ("-h".equals(nodeId) || "--help".equals(nodeId)) {
            HelpInfo.addSealerHelp();
            return;
        }
        if (nodeId.length() != 128) {
            ConsoleUtils.printJson(
                    PrecompiledCommon.transferToJson(PrecompiledCommon.InvalidNodeId));
        } else {
            ConsensusService consensusService = new ConsensusService(web3j, credentials);
            String result;
            result = consensusService.addSealer(nodeId);
            ConsoleUtils.printJson(result);
        }
        System.out.println();
    }

    @Override
    public void addObserver(String[] params) throws Exception {

        if (params.length < 2) {
            HelpInfo.promptHelp("addObserver");
            return;
        }
        if (params.length > 2) {
            HelpInfo.promptHelp("addObserver");
            return;
        }
        String nodeId = params[1];
        if ("-h".equals(nodeId) || "--help".equals(nodeId)) {
            HelpInfo.addObserverHelp();
            return;
        }
        if (nodeId.length() != 128) {
            ConsoleUtils.printJson(
                    PrecompiledCommon.transferToJson(PrecompiledCommon.InvalidNodeId));
        } else {
            ConsensusService consensusService = new ConsensusService(web3j, credentials);
            String result = consensusService.addObserver(nodeId);
            ConsoleUtils.printJson(result);
        }
        System.out.println();
    }

    @Override
    public void removeNode(String[] params) throws Exception {
        if (params.length < 2) {
            HelpInfo.promptHelp("removeNode");
            return;
        }
        if (params.length > 2) {
            HelpInfo.promptHelp("removeNode");
            return;
        }
        String nodeId = params[1];
        if ("-h".equals(nodeId) || "--help".equals(nodeId)) {
            HelpInfo.removeNodeHelp();
            return;
        }
        if (nodeId.length() != 128) {
            ConsoleUtils.printJson(
                    PrecompiledCommon.transferToJson(PrecompiledCommon.InvalidNodeId));
        } else {
            ConsensusService consensusService = new ConsensusService(web3j, credentials);
            String result = null;
            result = consensusService.removeNode(nodeId);
            ConsoleUtils.printJson(result);
        }
        System.out.println();
    }

    @Override
    public void setSystemConfigByKey(String[] params) throws Exception {
        if (params.length < 2) {
            HelpInfo.promptHelp("setSystemConfigByKey");
            return;
        }
        if (params.length > 3) {
            HelpInfo.promptHelp("setSystemConfigByKey");
            return;
        }
        String key = params[1];
        if ("-h".equals(key) || "--help".equals(key)) {
            HelpInfo.setSystemConfigByKeyHelp();
            return;
        }
        if (params.length < 3) {
            HelpInfo.promptHelp("setSystemConfigByKey");
            return;
        }
        if (Common.TxCountLimit.equals(key) || Common.TxGasLimit.equals(key)) {
            String valueStr = params[2];
            int value = 1;
            try {
                value = Integer.parseInt(valueStr);
                if (Common.TxCountLimit.equals(key)) {
                    if (value <= 0) {
                        System.out.println(
                                "Please provide value by positive integer mode, "
                                        + Common.PositiveIntegerRange
                                        + ".");
                        System.out.println();
                        return;
                    }
                } else {
                    if (value < Common.TxGasLimitMin) {
                        System.out.println(
                                "Please provide value by positive integer mode, "
                                        + Common.TxGasLimitRange
                                        + ".");
                        System.out.println();
                        return;
                    }
                }
                SystemConfigSerivce systemConfigSerivce =
                        new SystemConfigSerivce(web3j, credentials);
                String result = systemConfigSerivce.setValueByKey(key, value + "");
                ConsoleUtils.printJson(result);
            } catch (NumberFormatException e) {
                if (Common.TxCountLimit.equals(key)) {
                    System.out.println(
                            "Please provide value by positive integer mode, "
                                    + Common.PositiveIntegerRange
                                    + ".");
                } else {
                    System.out.println(
                            "Please provide value by positive integer mode, "
                                    + Common.TxGasLimitRange
                                    + ".");
                }
                System.out.println();
                return;
            }
        } else {
            System.out.println(
                    "Please provide a valid key, for example: "
                            + Common.TxCountLimit
                            + " or "
                            + Common.TxGasLimit
                            + ".");
        }
        System.out.println();
    }

    @Override
    public void desc(String[] params) {
        if (params.length < 2) {
            HelpInfo.promptHelp("desc");
            return;
        }
        if (params.length > 2) {
            HelpInfo.promptHelp("desc");
            return;
        }
        String tableName = params[1];
        if ("-h".equals(tableName) || "--help".equals(tableName)) {
            HelpInfo.showDescHelp();
            return;
        }
        try {
            CRUDSerivce crudSerivce = new CRUDSerivce(web3j, credentials);
            Table descTable = crudSerivce.desc(tableName);
            if (descTable.getKey() == null) {
                System.out.println("The table '" + tableName + "' does not exist.");
                System.out.println();
                return;
            }
            ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
            String tableInfo =
                    objectMapper.writeValueAsString(
                            new TableInfo(descTable.getKey(), descTable.getValueFields()));
            ConsoleUtils.printJson(tableInfo);
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        }
    }

    @Override
    public void createTable(String sql) {
        Table table = new Table();
        try {
            CRUDParseUtils.parseCreateTable(sql, table);
        } catch (ConsoleMessageException e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        } catch (JSQLParserException e) {
            System.out.println("Could not parse SQL statement.");
            System.out.println();
            return;
        }
        try {
            CRUDSerivce crudSerivce = new CRUDSerivce(web3j, credentials);
            int result = crudSerivce.createTable(table);
            if (result == 0) {
                System.out.println("Create '" + table.getTableName() + "' Ok.");
            } else if (result == PrecompiledCommon.TableExist) {
                System.out.println("The table '" + table.getTableName() + "' already exists.");
            } else if (result == Common.PermissionCode) {
                ConsoleUtils.printJson(PrecompiledCommon.transferToJson(Common.PermissionCode));
            } else {
                System.out.println("Create '" + table.getTableName() + "' failed.");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        }
    }

    @Override
    public void insert(String sql) {
        CRUDSerivce crudSerivce = new CRUDSerivce(web3j, credentials);
        Table table = new Table();
        Entry entry = table.getEntry();
        boolean useValues = false;
        try {
            useValues = CRUDParseUtils.parseInsert(sql, table, entry);
        } catch (ConsoleMessageException e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        } catch (JSQLParserException e) {
            System.out.println("Could not parse SQL statement.");
            System.out.println();
            return;
        }
        try {
            String tableName = table.getTableName();
            Table descTable = crudSerivce.desc(tableName);
            String keyName = descTable.getKey();
            String fields = keyName + "," + descTable.getValueFields();
            List<String> fieldsList = Arrays.asList(fields.split(","));
            Set<String> entryFields = entry.getFields().keySet();
            // insert into t_test values (fruit, 1, apple)
            if (useValues) {
                if (entry.getFields().size() != fieldsList.size()) {
                    throw new ConsoleMessageException("Column count doesn't match value count.");
                } else {
                    Entry entryValue = table.getEntry();
                    for (int i = 0; i < entry.getFields().size(); i++) {
                        for (String entryField : entryFields) {
                            if ((i + "").equals(entryField)) {
                                entryValue.put(fieldsList.get(i), entry.get(i + ""));
                                if (keyName.equals(fieldsList.get(i))) {
                                    table.setKey(entry.get(i + ""));
                                }
                            }
                        }
                    }
                    entry = entryValue;
                }
            }
            // insert into t_test (name, item_id, item_name) values (fruit, 1, apple)
            else {
                for (String entryField : entryFields) {
                    if (!fieldsList.contains(entryField)) {
                        throw new ConsoleMessageException(
                                "Unknown field '" + entryField + "' in field list.");
                    }
                }
                String keyValue = entry.get(keyName);
                if (keyValue == null) {
                    throw new ConsoleMessageException(
                            "Please insert the key field '" + keyName + "'.");
                }
                table.setKey(keyValue);
            }
            int insertResult = crudSerivce.insert(table, entry);
            if (insertResult == 0 || insertResult == 1) {
                System.out.println("Insert OK, " + insertResult + " row affected.");
            } else if (insertResult == Common.PermissionCode) {
                ConsoleUtils.printJson(PrecompiledCommon.transferToJson(Common.PermissionCode));
            } else {
                System.out.println("Insert failed.");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        }
    }

    @Override
    public void update(String sql) {
        CRUDSerivce crudSerivce = new CRUDSerivce(web3j, credentials);
        Table table = new Table();
        Entry entry = table.getEntry();
        Condition condition = table.getCondition();
        try {
            CRUDParseUtils.parseUpdate(sql, table, entry, condition);
        } catch (ConsoleMessageException e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        } catch (JSQLParserException e) {
            System.out.println("Could not parse SQL statement.");
            System.out.println();
            return;
        }
        try {
            String tableName = table.getTableName();
            Table descTable = crudSerivce.desc(tableName);
            String keyName = descTable.getKey();
            if (entry.getFields().containsKey(keyName)) {
                throw new ConsoleMessageException(
                        "Please don't set the key field '" + keyName + "'.");
            }
            table.setKey(descTable.getKey());
            handleKey(table, condition);
            String fields = descTable.getKey() + "," + descTable.getValueFields();
            List<String> fieldsList = Arrays.asList(fields.split(","));
            Set<String> entryFields = entry.getFields().keySet();
            Set<String> conditonFields = condition.getConditions().keySet();
            Set<String> allFields = new HashSet<>();
            allFields.addAll(entryFields);
            allFields.addAll(conditonFields);
            for (String entryField : allFields) {
                if (!fieldsList.contains(entryField)) {
                    throw new ConsoleMessageException(
                            "Unknown field '" + entryField + "' in field list.");
                }
            }
            int updateResult = crudSerivce.update(table, entry, condition);
            if (updateResult == 0 || updateResult == 1) {
                System.out.println("Update OK, " + updateResult + " row affected.");
            } else if (updateResult == Common.PermissionCode) {
                ConsoleUtils.printJson(PrecompiledCommon.transferToJson(Common.PermissionCode));
            } else {
                System.out.println("Update OK, " + updateResult + " rows affected.");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        }
    }

    @Override
    public void remove(String sql) {
        CRUDSerivce crudSerivce = new CRUDSerivce(web3j, credentials);
        Table table = new Table();
        Condition condition = table.getCondition();
        try {
            CRUDParseUtils.parseRemove(sql, table, condition);
        } catch (ConsoleMessageException e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        } catch (JSQLParserException e) {
            System.out.println("Could not parse SQL statement.");
            System.out.println();
            return;
        }
        try {
            Table descTable = crudSerivce.desc(table.getTableName());
            table.setKey(descTable.getKey());
            handleKey(table, condition);
            int removeResult = crudSerivce.remove(table, condition);
            if (removeResult == 0 || removeResult == 1) {
                System.out.println("Remove OK, " + removeResult + " row affected.");
            } else if (removeResult == Common.PermissionCode) {
                ConsoleUtils.printJson(PrecompiledCommon.transferToJson(Common.PermissionCode));
            } else {
                System.out.println("Remove OK, " + removeResult + " rows affected.");
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        }
    }

    @Override
    public void select(String sql) {
        Table table = new Table();
        Condition condition = table.getCondition();
        List<String> selectColumns = new ArrayList<>();
        try {
            CRUDParseUtils.parseSelect(sql, table, condition, selectColumns);
        } catch (ConsoleMessageException e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        } catch (JSQLParserException e) {
            System.out.println("Could not parse SQL statement.");
            System.out.println();
            return;
        }
        CRUDSerivce crudSerivce = new CRUDSerivce(web3j, credentials);
        try {
            Table descTable = crudSerivce.desc(table.getTableName());
            table.setKey(descTable.getKey());
            handleKey(table, condition);
            List<Map<String, String>> result = crudSerivce.select(table, condition);
            int rows = 0;
            if (result.size() == 0) {
                System.out.println("Empty set.");
                System.out.println();
                return;
            }
            result = filterSystemColum(result);
            if ("*".equals(selectColumns.get(0))) {
                result.stream().forEach(System.out::println);
                rows = result.size();
            } else {
                List<Map<String, String>> selectedResult = getSeletedColumn(selectColumns, result);
                rows = selectedResult.size();
            }
            if (rows == 1) {
                System.out.println(rows + " row in set.");
            } else {
                System.out.println(rows + " rows in set.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println();
            return;
        }
        System.out.println();
    }

    private List<Map<String, String>> getSeletedColumn(
            List<String> selectColumns, List<Map<String, String>> result) {
        List<Map<String, String>> selectedResult = new ArrayList<>(result.size());
        Map<String, String> selectedRecords;
        for (Map<String, String> records : result) {
            selectedRecords = new LinkedHashMap<>();
            for (String column : selectColumns) {
                Set<String> recordKeys = records.keySet();
                for (String recordKey : recordKeys) {
                    if (recordKey.equals(column)) {
                        selectedRecords.put(recordKey, records.get(recordKey));
                    }
                }
            }
            selectedResult.add(selectedRecords);
        }
        selectedResult.stream().forEach(System.out::println);
        return selectedResult;
    }

    private List<Map<String, String>> filterSystemColum(List<Map<String, String>> result) {

        List<String> filteredColumns = Arrays.asList("_hash_", "_status_");
        List<Map<String, String>> filteredResult = new ArrayList<>(result.size());
        Map<String, String> filteredRecords;
        for (Map<String, String> records : result) {
            filteredRecords = new LinkedHashMap<>();
            Set<String> recordKeys = records.keySet();
            for (String recordKey : recordKeys) {
                if (!filteredColumns.contains(recordKey)) {
                    filteredRecords.put(recordKey, records.get(recordKey));
                }
            }
            filteredResult.add(filteredRecords);
        }
        return filteredResult;
    }

    private void handleKey(Table table, Condition condition) throws Exception {

        String keyName = table.getKey();
        String keyValue = "";
        Map<EnumOP, String> keyMap = condition.getConditions().get(keyName);
        if (keyMap == null) {
            throw new ConsoleMessageException(
                    "Please provide a equal condition for the key field '"
                            + keyName
                            + "' in where clause.");
        } else {
            Set<EnumOP> keySet = keyMap.keySet();
            for (EnumOP enumOP : keySet) {
                if (enumOP != EnumOP.eq) {
                    throw new ConsoleMessageException(
                            "Please provide a equal condition for the key field '"
                                    + keyName
                                    + "' in where clause.");
                } else {
                    keyValue = keyMap.get(enumOP);
                }
            }
        }
        table.setKey(keyValue);
    }

    private List<Map<String, String>> getTableNames() throws Exception {
        CRUDSerivce crudSerivce = new CRUDSerivce(web3j, credentials);
        Table table = new Table();
        table.setTableName("_sys_tables_");
        table.setKey("table_name");
        Condition condition = table.getCondition();
        List<Map<String, String>> userTable = crudSerivce.select(table, condition);
        return userTable;
    }
}