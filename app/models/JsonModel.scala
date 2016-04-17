package models

import play.api.libs.json.Format

trait JsonModel[T] {
  
  val format: Format[T]

}