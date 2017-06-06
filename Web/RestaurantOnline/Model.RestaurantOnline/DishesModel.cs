using System;
namespace Model.RestaurantOnline
{
    public class DishesModel
    {
        private int id;
        private int categoryId;
        private string eName;
        private string otherName;
        private decimal price;
        private string eComment;
        private string otherComment;
        private string cEName;
        private string cOName;
        private String modifyTime;

        public int Id{
            get{
                return id;
            }
            set{
                this.id = value;
            }
        }

        public int CategoryId{
            get{return categoryId;}
            set{this.categoryId = value;}
        }

        public string EName{
            get{return eName;}
            set{this.eName = value;}
        }

        public string OtherName{
            get{return otherName;}
            set{this.otherName = value;}
        }

        public decimal Price{
            get{return price;}
            set{this.price = value;}
        }

        public string EComment{
            get{return eComment;}
            set{this.eComment = value;}
        }

        public string OtherComment{
            get{return otherComment;}
            set{this.otherComment = value;}
        }

        public string CEName{
            get{return cEName;}
            set{this.cEName = value;}
        }

        public string COName{
            get{return cOName;}
            set{this.cOName = value;}
        }

        public String ModifyTime
        {
            get { return modifyTime; }
            set { this.modifyTime = value; }
        }

        public String ImgUrl { get; set; }
    }
}
