package km.i18n.util.conf


case class I18nConf(srcDirs: Seq[String],
                    srcFileExtension: Seq[String],
                    keyword: KeywordConf, destDir: String,
                    kmdmErrorCodeConf: KmdmErrorCodeConf,
                    integrationModuleErrorCodeConf: IntegrationModuleErrorCodeConf,
                    authorityErrorCodeConf: AuthorityErrorCodeConf,
                    dbConf: DBConf,
                    codeStartIdx: Int)
