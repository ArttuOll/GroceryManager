package com.bsuuv.grocerymanager.logic;

//TODO: rewrite
/*@RunWith(MockitoJUnitRunner.class)
public class FoodSchedulerTests {

    private FoodItemEntity mCorrectFoodItem1;
    private FoodItemEntity mCorrectFoodItem2;
    private FoodItemEntity mCorrectFoodItem3;
    private FoodItemEntity mFaultyFoodItem1;
    private List<FoodItemEntity> mFoodItems;
    @Mock
    SharedPreferencesHelper sharedPrefsHelper;
    private Set<String> mGroceryDays;
    private Map<FoodItemEntity, Double> mFoodItemTracker;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        this.mFoodItems = new ArrayList<>();
        this.mCorrectFoodItem1 = new FoodItemEntity(1, "Juusto", "Valio", "Maukasta",
                3, "Cans", 4, 2, "");
        this.mCorrectFoodItem2 = new FoodItemEntity(2, "Kurkku", "", "",
                1, "Cans", 2, 2, "");
        this.mCorrectFoodItem3 = new FoodItemEntity(3, "Juusto", "Valio", "Maukasta",
                3, "Cans", 1, 1, "");
        this.mFaultyFoodItem1 = new FoodItemEntity(4, "Olut", "Karjala", "Helvetin hyvää",
                3, "Cans", 1, 3, "");
        this.mGroceryDays = new HashSet<>();
        this.mFoodItemTracker = new HashMap<>();

        when(sharedPrefsHelper.getFoodItemTracker()).thenReturn(mFoodItemTracker);
        when(sharedPrefsHelper.getFoodItems()).thenReturn(mFoodItems);
        when(sharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);
    }

    @Test
    public void returnsCorrectGroceryListOnGroceryDay() {
        // Add current weekday as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));
        mFoodItems.add(mCorrectFoodItem1);
        mFoodItemTracker.put(mCorrectFoodItem1, 0.5);

        FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);

        // When calling .getGroceryList for the first time, frequency quotient for mCorrectFoodItem1
        // is 0.5, so an empty list is returned.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
        // When calling the second time, frequency quotient has been incremented, so list containing
        // mCorrectFoodItem1 is returned.
        Assert.assertEquals(mFoodItems, foodScheduler.getGroceryList());
    }

    @Test
    public void returnsEmptyGroceryListOnNotGroceryDay() {
        int tomorrow = (getTodayInt() != 7) ? (getTodayInt() + 1) : 1;

        mGroceryDays.add(intWeekDayToString(tomorrow));
        mFoodItems.add(mCorrectFoodItem1);
        mFoodItemTracker.put(mCorrectFoodItem1, 0.5);

        FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);

        // When calling .getGroceryList for the first time, frequency quotient for mCorrectFoodItem1
        // is 0.5, so an empty list is always returned.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
        // When calling second time, empty list should still be returned, since it's not grocery
        // day.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
    }

    @Test
    public void generatesCorrectQuotientMap() {
        // Add current weekday as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));
        mFoodItems.add(mCorrectFoodItem1);
        mFoodItems.add(mCorrectFoodItem2);
        mFoodItems.add(mCorrectFoodItem3);
        // Override previous stubbing of the same method. Null is required for
        // FoodScheduler.getFoodItemTracker to create a new quotinent map.
        when(sharedPrefsHelper.getFoodItemTracker()).thenReturn(new HashMap<>());

        FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);

        Map<FoodItemEntity, Double> expected = new HashMap<>();
        expected.put(mCorrectFoodItem1, 0.5);
        expected.put(mCorrectFoodItem2, 1.0);
        expected.put(mCorrectFoodItem3, 1.0);

        Assert.assertEquals(expected, foodScheduler.getFoodItemTracker());
    }

    @Test
    public void throwsExceptionWhenFaultyFoodItemQuotient() {
        // Add current weekday as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));
        mFoodItems.add(mFaultyFoodItem1);
        // Doesn't matter what is returned when getFoodItemTracker is called,
        // getFoodItemFrequencyQuotient is called anyway.
        when(sharedPrefsHelper.getFoodItemTracker()).thenReturn(new HashMap<>());

        boolean throwsException = false;

        try {
            FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);
            foodScheduler.getFoodItemTracker();
        } catch (UnsupportedOperationException exception) {
            throwsException = true;
        }

        Assert.assertTrue(throwsException);
    }

    private int getTodayInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private String intWeekDayToString(int groceryDay) {
        switch (groceryDay) {
            case 1:
                return "sunday";
            case 2:
                return "monday";
            case 3:
                return "tuesday";
            case 4:
                return "wednesday";
            case 5:
                return "thursday";
            case 6:
                return "friday";
            case 7:
                return "saturday";
            default:
                return "";
        }
    }
}*/
