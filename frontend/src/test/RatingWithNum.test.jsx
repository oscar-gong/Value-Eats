import { shallow } from "enzyme";
import React from "react";
import renderer from "react-test-renderer";
import RatingWithNum from "../components/RatingWithNum";

describe("RatingWithNum", () => {
  it("can work in the base case", () => {
    const rating = shallow(<RatingWithNum rating={5.0}/>);
    expect(rating.exists()).toBeTruthy();
  });

  it("can handle a rating of 5 correctly", () => {
    const rating = shallow(<RatingWithNum rating={5.0}/>);
    // console.log(rating.debug({ verbose: true }));
    expect(rating.text()).toBe("<StarRating />5");
  });

  it("can handle a rating with a float value correctly", () => {
    const rating = shallow(<RatingWithNum rating={2.5}/>);
    // console.log(rating.debug({ verbose: true }));
    expect(rating.text()).toBe("<StarRating />2.5");
  });

  it("can handle a rating of 0 correctly (should not show decimal point and should instead show whole number", () => {
    const rating = shallow(<RatingWithNum rating={0.0}/>);
    // console.log(rating.debug({ verbose: true }));
    expect(rating.text()).toBe("<StarRating />0");
  });

  /* Snapshot tests - creates snapshots for new tests, otherwise will check the existing tests. To update existing tests,
  follow the prompts on the screen after running test. Done to avoid regression. */

  it("renders with minimal props", () => {
    const rating = renderer.create(<RatingWithNum rating={5.0}/>);
    expect(rating).toMatchSnapshot();
  });

  it("renders with double star rating", () => {
    const rating = renderer.create(<RatingWithNum rating={2.5}/>);
    expect(rating).toMatchSnapshot();
  });

  it("renders correctly when editable", () => {
    const rating = renderer.create(<RatingWithNum rating={0.0}/>);
    expect(rating).toMatchSnapshot();
  });
});
