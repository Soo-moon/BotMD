import DTO.Auctions.Options.AuctionsOption;
import DTO.Auctions.items.Auction;
import DTO.Auctions.items.RequestAuctionItems;
import DTO.Market.MarketList;
import DTO.Market.RequestMarketItems;
import retrofit2.Call;
import retrofit2.http.*;

public interface APIService {

    @GET("/auctions/options")
    Call<AuctionsOption> auctionsOptions();

    @POST("/auctions/items")
    Call<Auction> auctions_Items(@Body RequestAuctionItems requestAuctionItems);

    @POST("/markets/items")
    Call<MarketList> searchItemPrice(@Body RequestMarketItems requestMarketItems);

    @FormUrlEncoded
    @POST("/markets/items")
    Call<MarketList> searchItemPrice(@Field("CategoryCode") String categoryCode , @Field("ItemGrade") String ItemGrade , @Field("ItemTier")Integer ItemTier, @Field("ItemName")String ItemName);
}

/** CategoryCode
 *             "Code": 10100,   "CodeName": "장비 상자"
 *             "Code": 20000,   "CodeName": "아바타"
 *             "Code": 40000,   "CodeName": "각인서"
 *             "Code": 50000,   "CodeName": "강화 재료"
 *             "Code": 60000,   "CodeName": "전투 용품"
 *             "Code": 70000,   "CodeName": "요리"
 *             "Code": 90000,   "CodeName": "생활"
 *             "Code": 100000,  "CodeName": "모험의 서"
 *             "Code": 110000,  "CodeName": "항해"
 *             "Code": 140000,  "CodeName": "펫"
 *             "Code": 160000,  "CodeName": "탈 것"
 *             "Code": 170000,  "CodeName": "기타"
 *             "Code": 220000,  "CodeName": "보석 상자"
 */
/** market catergories
 *     "Categories": [
 *         {
 *             "Subs": [],
 *             "Code": 10100,
 *             "CodeName": "장비 상자"
 *         },
 *         {
 *             "Subs": [
 *                 {
 *                     "Code": 20005,
 *                     "CodeName": "무기"
 *                 },
 *                 {
 *                     "Code": 20010,
 *                     "CodeName": "머리"
 *                 },
 *                 {
 *                     "Code": 20020,
 *                     "CodeName": "얼굴1"
 *                 },
 *                 {
 *                     "Code": 20030,
 *                     "CodeName": "얼굴2"
 *                 },
 *                 {
 *                     "Code": 20050,
 *                     "CodeName": "상의"
 *                 },
 *                 {
 *                     "Code": 20060,
 *                     "CodeName": "하의"
 *                 },
 *                 {
 *                     "Code": 20070,
 *                     "CodeName": "상하의 세트"
 *                 },
 *                 {
 *                     "Code": 21400,
 *                     "CodeName": "악기"
 *                 },
 *                 {
 *                     "Code": 21500,
 *                     "CodeName": "아바타 상자"
 *                 },
 *                 {
 *                     "Code": 21600,
 *                     "CodeName": "이동 효과"
 *                 }
 *             ],
 *             "Code": 20000,
 *             "CodeName": "아바타"
 *         },
 *         {
 *             "Subs": [],
 *             "Code": 40000,
 *             "CodeName": "각인서"
 *         },
 *         {
 *             "Subs": [
 *                 {
 *                     "Code": 50010,
 *                     "CodeName": "재련 재료"
 *                 },
 *                 {
 *                     "Code": 50020,
 *                     "CodeName": "재련 추가 재료"
 *                 },
 *                 {
 *                     "Code": 51000,
 *                     "CodeName": "기타 재료"
 *                 },
 *                 {
 *                     "Code": 51100,
 *                     "CodeName": "무기 진화 재료"
 *                 }
 *             ],
 *             "Code": 50000,
 *             "CodeName": "강화 재료"
 *         },
 *         {
 *             "Subs": [
 *                 {
 *                     "Code": 60200,
 *                     "CodeName": "배틀 아이템 -회복형"
 *                 },
 *                 {
 *                     "Code": 60300,
 *                     "CodeName": "배틀 아이템 -공격형"
 *                 },
 *                 {
 *                     "Code": 60400,
 *                     "CodeName": "배틀 아이템 -기능성"
 *                 },
 *                 {
 *                     "Code": 60500,
 *                     "CodeName": "배틀 아이템 -버프형"
 *                 }
 *             ],
 *             "Code": 60000,
 *             "CodeName": "전투 용품"
 *         },
 *         {
 *             "Subs": [],
 *             "Code": 70000,
 *             "CodeName": "요리"
 *         },
 *         {
 *             "Subs": [
 *                 {
 *                     "Code": 90200,
 *                     "CodeName": "식물채집 전리품"
 *                 },
 *                 {
 *                     "Code": 90300,
 *                     "CodeName": "벌목 전리품"
 *                 },
 *                 {
 *                     "Code": 90400,
 *                     "CodeName": "채광 전리품"
 *                 },
 *                 {
 *                     "Code": 90500,
 *                     "CodeName": "수렵 전리품"
 *                 },
 *                 {
 *                     "Code": 90600,
 *                     "CodeName": "낚시 전리품"
 *                 },
 *                 {
 *                     "Code": 90700,
 *                     "CodeName": "고고학 전리품"
 *                 },
 *                 {
 *                     "Code": 90800,
 *                     "CodeName": "기타"
 *                 }
 *             ],
 *             "Code": 90000,
 *             "CodeName": "생활"
 *         },
 *         {
 *             "Subs": [],
 *             "Code": 100000,
 *             "CodeName": "모험의 서"
 *         },
 *         {
 *             "Subs": [
 *                 {
 *                     "Code": 110100,
 *                     "CodeName": "선박 재료"
 *                 },
 *                 {
 *                     "Code": 110110,
 *                     "CodeName": "선박 스킨"
 *                 },
 *                 {
 *                     "Code": 111900,
 *                     "CodeName": "선박 재료 상자"
 *                 }
 *             ],
 *             "Code": 110000,
 *             "CodeName": "항해"
 *         },
 *         {
 *             "Subs": [
 *                 {
 *                     "Code": 140100,
 *                     "CodeName": "펫"
 *                 },
 *                 {
 *                     "Code": 140200,
 *                     "CodeName": "펫 상자"
 *                 }
 *             ],
 *             "Code": 140000,
 *             "CodeName": "펫"
 *         },
 *         {
 *             "Subs": [
 *                 {
 *                     "Code": 160100,
 *                     "CodeName": "탈 것"
 *                 },
 *                 {
 *                     "Code": 160200,
 *                     "CodeName": "탈 것 상자"
 *                 }
 *             ],
 *             "Code": 160000,
 *             "CodeName": "탈 것"
 *         },
 *         {
 *             "Subs": [],
 *             "Code": 170000,
 *             "CodeName": "기타"
 *         },
 *         {
 *             "Subs": [],
 *             "Code": 220000,
 *             "CodeName": "보석 상자"
 *         }
 *     ],
 */

/**     auctionsCategories
 * "Categories": [
 {
 "Subs": [
 {
 "Code": 170300,
 "CodeName": "아뮬렛"
 },
 {
 "Code": 180000,
 "CodeName": "무기"
 },
 {
 "Code": 190010,
 "CodeName": "투구"
 },
 {
 "Code": 190020,
 "CodeName": "상의"
 },
 {
 "Code": 190030,
 "CodeName": "하의"
 },
 {
 "Code": 190040,
 "CodeName": "장갑"
 },
 {
 "Code": 190050,
 "CodeName": "어깨"
 }
 ],
 "Code": 10000,
 "CodeName": "장비"
 },
 {
 "Subs": [],
 "Code": 30000,
 "CodeName": "어빌리티 스톤"
 },
 {
 "Subs": [
 {
 "Code": 200010,
 "CodeName": "목걸이"
 },
 {
 "Code": 200020,
 "CodeName": "귀걸이"
 },
 {
 "Code": 200030,
 "CodeName": "반지"
 },
 {
 "Code": 200040,
 "CodeName": "팔찌"
 }
 ],
 "Code": 200000,
 "CodeName": "장신구"
 },
 {
 "Subs": [],
 "Code": 210000,
 "CodeName": "보석"
 }
 ],
 *
 */
