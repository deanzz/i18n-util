package km.i18n.util.module.dao

import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.reflect.ClassTag

abstract class AbstractDao[T <: AnyRef: ClassTag](uri: String, dbName: String) {
  lazy val mongodb: MongoClient = MongoClient(uri)
  protected val defaultRegistry: CodecRegistry = fromRegistries(
    DEFAULT_CODEC_REGISTRY
  )
  protected def collectionName: String
  protected def getDataBase: MongoDatabase = mongodb.getDatabase(dbName)
  protected def getCollection(implicit codecRegistry: CodecRegistry): MongoCollection[T] =
    getDataBase.getCollection[T](collectionName).withCodecRegistry(codecRegistry)
}