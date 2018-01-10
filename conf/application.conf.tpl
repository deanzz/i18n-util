source{
  //"/Users/deanzhang/work/code/km/elemental/spark-jobs/src/main",
  dirs = [
    "/Users/deanzhang/work/code/km/elemental/message-rest/app"
    //"/Users/deanzhang/work/code/km/elemental/authority/app",
    //"/Users/deanzhang/work/code/km/elemental/datacenter/app",
    //"/Users/deanzhang/work/code/km/elemental/elemental-rest/app",
    //"/Users/deanzhang/work/code/km/elemental/storage-server/app",
    //"/Users/deanzhang/work/code/km/kmdm/kmdm-rest/src/main",
    //"/Users/deanzhang/work/code/km/kmdm/kmdm-common/src/main",
    //"/Users/deanzhang/work/code/km/kmdm/kmdm-project-manager/src/main"
  ]

  file-extension = [".java", ".scala"]

  keyword{
    key = """new KMException"""
    regex1 = """new KMException\(([-]?\d+)\s*,\s*(.+)\)"""
    regex2 = """new KMException\(KmdmErrorCode\.(.+)\s*,\s*(((?!KmdmErrorCode).)*)\)"""
    regex3 = """new KMException\(KmdmErrorCode\.(.+)\s*,\s*(.+\s*\+\s*KmdmErrorCode\.[_\w]+)\)"""
    regex4 = """new KMException\(KmdmErrorCode\.(.+)\s*,\s*(KmdmErrorCode\.[_\w]+\s*\+.+)\)"""
    regex5 = """new KMException\(KmdmErrorCode\.(.+)\s*,\s*(.+\s*\+\s*KmdmErrorCode\.[_\w]+\s*\+.+)\)"""
    regex6 = """new KMException\(AuthorityErrorCode\.(.+)\s*,\s*(((?!AuthorityErrorCode).)*)\)"""
    regex7 = """new KMException\(KmdmErrorCode\.(.+)\s*,\s*KmdmErrorCode\.(.+)\)"""
    regex8 = """new KMException\(IntegrationModuleErrorCode\.(.+)\s*,\s*IntegrationModuleErrorCode\.(.+)\)"""
    regex9 = """new KMException\(AuthorityErrorCode\.(.+)\s*,\s*AuthorityErrorCode\.(.+)\)"""
    regex10 = """new KMException\(KmdmErrorCode\.(.+)\s*,(\s*)$"""
    regex11 = """new KMException\(IntegrationModuleErrorCode\.(.+)\s*,(\s*)$"""
    regex12 = """new KMException\(AuthorityErrorCode\.(.+)\s*,(\s*)$"""
  }

  KmdmErrorCode{
    file-path = "xxx/KmdmErrorCode.java"
    msg-regex = """String\s+(.+)_[MSG|STR]+\s?=\s?(.+);"""
  }

  IntegrationModuleErrorCode{
    file-path = "xxx/IntegrationModuleErrorCode.java"
    msg-regex = """String\s+(.+)_STR\s?=\s?(.+);"""
  }

  AuthorityErrorCode{
    file-path = "xxx/AuthorityErrorCode.scala"
    msg-regex = """val\s+(.+)_MSG\s?=\s?(.+)"""
  }

  code-start-idx = 1000324
}

destination{
  dir = "xxx/i18n-util/dest"
}

mongodb{

  uri = "mongodb://username:password@host:port/schema?connectTimeoutMS=30000&wtimeoutMS=360000"
  name = "schema name"
}
