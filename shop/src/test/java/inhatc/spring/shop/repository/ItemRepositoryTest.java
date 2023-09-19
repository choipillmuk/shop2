package inhatc.spring.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import inhatc.spring.shop.constant.ItemSellStatus;
import inhatc.spring.shop.entity.Item;
import inhatc.spring.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Querydsl 테스트")
    public void queryDslTest(){
        createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        List<Item> itemList = queryFactory.selectFrom(QItem.item)
                .where(QItem.item.itemSellStatus.eq(ItemSellStatus.Sell))
                .where(QItem.item.itemDetail.like("%" + "테스트" + "%"))
                .orderBy(QItem.item.price.desc())
                .fetch();

        itemList.forEach(System.out::println);
    }


    public void createItemList(){
        for (int i = 1; i <= 10; i++) {
            Item item = Item.builder()
                    .itemNm("테스트 상품" + i)
                    .price(10000 + i)
                    .stockNumber(100 + i)
                    .itemDetail("테스트 상품에 대한 상세설명" + i)
                    .itemSellStatus(ItemSellStatus.Sell)
                    .regTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("JPQL 테스트")
    public void findByDetailTest(){
        createItemList();
        List<Item> detailList = itemRepository.findByDetail("1");
        detailList.forEach(System.out::println);
    }

    @Test
    @DisplayName("JPQL Native 테스트")
    public void findByDetailNativeTest(){
        createItemList();
        List<Item> detailList = itemRepository.findByDetailNative("1");
        detailList.forEach(System.out::println);
    }

    @Test
    @DisplayName("가격에 대한 정렬 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        createItemList();
        List<Item> priceLessThanOrderByPriceDesc = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        priceLessThanOrderByPriceDesc.forEach(System.out::println);
    }

    @Test
    @DisplayName("상품명 또는 상품 상세 설명 조회 테스트")
    public void findByItemNmOrItemDetailTest(){
        createItemList();
        List<Item> itemList =
        itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        //itemList.forEach(item -> System.out.println(item));
        itemList.forEach(System.out::println);
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        createItemList();
        List<Item> itemList =  itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("상품 생성 테스트")
    public void createItemTest(){
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .stockNumber(100)
                .itemDetail("테스트 상품에 대한 상세설명")
                .itemSellStatus(ItemSellStatus.Sell)
                .stockNumber(100)
                .regTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        System.out.println("======================= item : " + item);
        Item savedItems = itemRepository.save(item);
        System.out.println("======================= savedItems : " + savedItems);
    }
}