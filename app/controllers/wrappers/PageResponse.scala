package controllers.models

case class PageResponse[T] (
   data: List[T],
   per_page: Int,
   page_count: Int,
   current_page: Int,
   total_data_count: Int,
   links: PageLinks
)

case class PageLinks (
   first: String,
   prev: Option[String],
   current: String,
   next: Option[String],
   last: String    
)