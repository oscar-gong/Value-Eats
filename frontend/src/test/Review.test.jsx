import React from "react";
import { shallow } from "enzyme";
import StoreProvider from "../utils/store";
import Review from "../components/Review";

describe("Review", () => {
  const noop = () => {};
  it("can work in the base case", () => {
    const review = shallow(
      <StoreProvider>
        <Review eateryId={0}
          username={"Test user"}
          profilePic={""}
          eateryName={"Test Eatery"}
          review={"This was good food!"}
          rating={5}
          images={[""]}
          onEateryProfile={true}
          isOwner={true}
          refreshList={noop}
          />
      </StoreProvider>
    );
    expect(review.exists()).toBeTruthy();
  });

  /* Snapshot tests - creates snapshots for new tests, otherwise will check the existing tests. To update existing tests,
  follow the prompts on the screen after running test. Done to avoid regression. */

  it("renders basic component", () => {
    const display = shallow(<StoreProvider>
      <Review eateryId={0}
        username={"Test user"}
        profilePic={""}
        eateryName={"Test Eatery"}
        review={"This was good food!"}
        rating={5}
        images={[""]}
        onEateryProfile={true}
        isOwner={true}
        refreshList={noop}
        />
    </StoreProvider>);
    expect(display).toMatchSnapshot();
  });
});
