package km.i18n.util.module.dao

import com.mongodb.client.model.UpdateOptions
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.bson.types.ObjectId
import org.mongodb.scala.Observer
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Updates.{combine, set}
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future

class ModuleInfoDao(uri: String, dbName: String) extends AbstractDao[ModuleInfo](uri, dbName){
  override protected def collectionName: String = "moduleInfo"
  import org.mongodb.scala.bson.codecs.Macros._
  private implicit val codecRegistry: CodecRegistry = fromRegistries(defaultRegistry, fromProviders(classOf[ModuleInfo]))

  def getAll: Future[Seq[ModuleInfo]] = getCollection.find().toFuture()

  def update(_id: ObjectId,
             nameEn: String,
             parentTypeEn: String,
             attributeEn: String,
             descriptionEn: String) = {
    val filter = equal("_id", _id)
    val update = combine(
      set("nameEn", nameEn),
      set("parentTypeEn", parentTypeEn),
      set("attributeEn", attributeEn),
      set("descriptionEn", descriptionEn)
    )
    getCollection
      .updateOne(filter, update, new UpdateOptions().upsert(false)).toFuture()
  }

  def update(nodeType: String, attribute: String) = {
    val filter = equal("nodeType", nodeType)
    val update = combine(
      set("attribute", attribute)
    )
    getCollection
      .updateOne(filter, update/*set("attribute", attribute)*/, new UpdateOptions().upsert(false)).toFuture()
  }

}

case class ModuleInfo(_id: ObjectId,
                      number: String,
                      name: String,
                      nameEn: Option[String] = None,
                      parentType: String,
                      parentTypeEn: Option[String] = None,
                      input: Seq[String],
                      output: Seq[String],
                      attribute: String,
                      attributeEn: Option[String] = None,
                      description: String,
                      descriptionEn: Option[String] = None,
                      paramType: Int,
                      priority: Int,
                      showRst: Boolean,
                      nodeVer: String,
                      nodeType: String,
                      workflowType: String,
                      isDefault: Int,
                      chargeConfig: Int,
                      storageType: Option[String] = None)
