package controllers.models

case class PageResponse[T] (
   items: List[T],
   links: Paging
)

case class Paging (
   offset: Int,
   limit: Int,
   Total: Int
)